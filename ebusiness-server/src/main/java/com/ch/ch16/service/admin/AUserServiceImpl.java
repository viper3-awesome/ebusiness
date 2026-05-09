package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.sercurity.utils.ConfigrarionBean;
import com.ch.ch16.common.sercurity.utils.JwtTokenUtil;
import com.ch.ch16.common.sercurity.utils.RedisUtil;
import com.ch.ch16.entity.AUserEntity;
import com.ch.ch16.repository.admin.AUserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("all")
public class AUserServiceImpl implements AUserService {
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConfigrarionBean config;
    @Autowired
    private AUserRepository aUserRepository;

    @Override
    public ResponseResult<Map<String, String>> login(AUserEntity aUserEntity) {
        Specification<AUserEntity> nameSpec = (root, query, cb) ->
                cb.equal(root.get("aname"), aUserEntity.getAname());
        long res = aUserRepository.count(nameSpec);
        if (res == 0) {
            return ResponseResult.getMessageResult(null, "A001");
        }
        Specification<AUserEntity> loginSpec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("aname"), aUserEntity.getAname()));
            predicates.add(cb.equal(root.get("apwd"), aUserEntity.getApwd()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<AUserEntity> mu = aUserRepository.findAll(loginSpec);
        if (mu.size() > 0) {
            String token = jwtUtil.createToken(aUserEntity.getAname());
            redisUtil.set("login_" + aUserEntity.getAname(), aUserEntity.getAname(), config.getRedisExpiration());
            Map<String, String> myres = new HashMap<>();
            myres.put("authtoken", token);
            myres.put("aname", aUserEntity.getAname());
            myres.put("aid", mu.get(0).getId() + "");
            return ResponseResult.getSuccessResult(myres);
        } else {
            return ResponseResult.getMessageResult(null, "A002");
        }
    }
}
