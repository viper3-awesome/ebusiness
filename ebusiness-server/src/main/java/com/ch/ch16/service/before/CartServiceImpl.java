package com.ch.ch16.service.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.CartEntity;
import com.ch.ch16.repository.before.CartRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("all")
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseResult<Map<String, Object>> addCart(CartEntity cartEntity) {
        Specification<CartEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("busertableId"), cartEntity.getBusertableId()));
            predicates.add(cb.equal(root.get("goodstableId"), cartEntity.getGoodstableId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        long n = cartRepository.count(spec);
        boolean b;
        if (n > 0) {
            // 购物车中已有该商品，更新数量
            List<CartEntity> existing = cartRepository.findAll(spec);
            CartEntity ce = existing.get(0);
            ce.setShoppingnum(ce.getShoppingnum() + cartEntity.getShoppingnum());
            cartRepository.save(ce);
            b = true;
        } else {
            b = cartRepository.save(cartEntity) != null;
        }
        if (b)
            return ResponseResult.getMessageResult(null, "A001");
        else
            return ResponseResult.getMessageResult(null, "A002");
    }

    @Override
    public ResponseResult<Map<String, Object>> bupDateCart(CartEntity cartEntity) {
        List<Integer> bcid = cartEntity.getBcid();
        List<Integer> bshoppingnum = cartEntity.getBshoppingnum();
        List<CartEntity> bCarts = new ArrayList<>();
        for (int i = 0; i < bcid.size(); i++) {
            CartEntity ce = cartRepository.findById(bcid.get(i)).orElse(null);
            if (ce != null) {
                ce.setShoppingnum(bshoppingnum.get(i));
                bCarts.add(ce);
            }
        }
        cartRepository.saveAll(bCarts);
        return ResponseResult.getMessageResult(null, "A001");
    }

    @Override
    public ResponseResult<Map<String, Object>> clearCart(CartEntity cartEntity) {
        Specification<CartEntity> spec = (root, query, cb) ->
                cb.equal(root.get("busertableId"), cartEntity.getBusertableId());
        List<CartEntity> list = cartRepository.findAll(spec);
        cartRepository.deleteAll(list);
        return ResponseResult.getMessageResult(null, "A001");
    }

    @Override
    public ResponseResult<Map<String, Object>> removeCart(CartEntity cartEntity) {
        cartRepository.deleteById(cartEntity.getId());
        return ResponseResult.getMessageResult(null, "A001");
    }

    @Override
    public long countByGoodsId(Integer goodsId) {
        Specification<CartEntity> spec = (root, query, cb) ->
                cb.equal(root.get("goodstableId"), goodsId);
        return cartRepository.count(spec);
    }
}
