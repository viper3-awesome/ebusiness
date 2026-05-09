package com.ch.ch16.service.before;

import com.ch.ch16.common.MD5Util;
import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.sercurity.utils.ConfigrarionBean;
import com.ch.ch16.common.sercurity.utils.JwtTokenUtil;
import com.ch.ch16.common.sercurity.utils.RedisUtil;
import com.ch.ch16.entity.BUserEntity;
import com.ch.ch16.repository.before.BUserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("all")
public class BUserServiceImpl implements BUserService {
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConfigrarionBean config;
    @Autowired
    private BUserRepository bUserRepository;

    @Override
    public ResponseResult<Map<String, String>> register(BUserEntity bUserEntity) {
        bUserEntity.setBpwd(MD5Util.MD5(bUserEntity.getBpwd()));
        Specification<BUserEntity> spec = (root, query, cb) ->
                cb.equal(root.get("bemail"), bUserEntity.getBemail());
        long n = bUserRepository.count(spec);
        if (n > 0) {
            return ResponseResult.getMessageResult(null, "A001");
        } else if (bUserRepository.save(bUserEntity) != null) {
            return ResponseResult.getMessageResult(null, "A002");
        } else {
            return ResponseResult.getMessageResult(null, "A003");
        }
    }

    @Override
    public ResponseResult<Map<String, String>> login(BUserEntity bUserEntity) {
        String rand = (String) redisUtil.get("code");
        if (!rand.equalsIgnoreCase(bUserEntity.getCode())) {
            return ResponseResult.getMessageResult(null, "A000");
        }
        Specification<BUserEntity> emailSpec = (root, query, cb) ->
                cb.equal(root.get("bemail"), bUserEntity.getBemail());
        long res = bUserRepository.count(emailSpec);
        if (res == 0) {
            return ResponseResult.getMessageResult(null, "A001");
        }
        bUserEntity.setBpwd(MD5Util.MD5(bUserEntity.getBpwd()));
        Specification<BUserEntity> loginSpec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("bemail"), bUserEntity.getBemail()));
            predicates.add(cb.equal(root.get("bpwd"), bUserEntity.getBpwd()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<BUserEntity> mu = bUserRepository.findAll(loginSpec);
        if (mu.size() > 0) {
            String token = jwtUtil.createToken(bUserEntity.getBemail());
            redisUtil.set("login_" + bUserEntity.getBemail(), bUserEntity.getBemail(), config.getRedisExpiration());
            Map<String, String> myres = new HashMap<>();
            myres.put("buserauthtoken", token);
            myres.put("bemail", bUserEntity.getBemail());
            myres.put("bid", mu.get(0).getId() + "");
            return ResponseResult.getSuccessResult(myres);
        } else {
            return ResponseResult.getMessageResult(null, "A002");
        }
    }
}
