package com.ch.ch16.service.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.FocusEntity;
import com.ch.ch16.repository.before.FocusRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("all")
public class FocusServiceImpl implements FocusService {
    @Autowired
    private FocusRepository focusRepository;

    @Override
    public ResponseResult<Map<String, Object>> focus(FocusEntity focusEntity) {
        Specification<FocusEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("goodstableId"), focusEntity.getGoodstableId()));
            predicates.add(cb.equal(root.get("busertableId"), focusEntity.getBusertableId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        long n = focusRepository.count(spec);
        if (n > 0)
            return ResponseResult.getMessageResult(null, "A001");
        focusEntity.setFocustime(new Date());
        if (focusRepository.save(focusEntity) != null)
            return ResponseResult.getMessageResult(null, "A002");
        return ResponseResult.getMessageResult(null, "A003");
    }

    @Override
    public long countByGoodsId(Integer goodsId) {
        Specification<FocusEntity> spec = (root, query, cb) ->
                cb.equal(root.get("goodstableId"), goodsId);
        return focusRepository.count(spec);
    }
}
