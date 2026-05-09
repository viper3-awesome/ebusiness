package com.ch.ch16.service.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.CartEntity;

import java.util.Map;

public interface CartService {
    ResponseResult<Map<String, Object>> addCart(CartEntity cartEntity);
    ResponseResult<Map<String, Object>> bupDateCart(CartEntity cartEntity);
    ResponseResult<Map<String, Object>> clearCart(CartEntity cartEntity);
    ResponseResult<Map<String, Object>> removeCart(CartEntity cartEntity);
    long countByGoodsId(Integer goodsId);
}
