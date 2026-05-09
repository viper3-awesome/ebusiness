package com.ch.ch16.service.admin;

import com.ch.ch16.entity.OrderdetailEntity;
import com.ch.ch16.repository.admin.OrderdetailRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@SuppressWarnings("all")
public class OrderdetailServiceImpl implements OrderdetailService {
    @Autowired
    private OrderdetailRepository orderdetailRepository;

    @Override
    public OrderdetailEntity save(OrderdetailEntity entity) {
        return orderdetailRepository.save(entity);
    }

    @Override
    public List<OrderdetailEntity> saveBatch(List<OrderdetailEntity> entities) {
        return orderdetailRepository.saveAll(entities);
    }

    @Override
    public long count(OrderdetailEntity example) {
        Specification<OrderdetailEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (example.getGoodstableId() != null)
                predicates.add(cb.equal(root.get("goodstableId"), example.getGoodstableId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return orderdetailRepository.count(spec);
    }
}
