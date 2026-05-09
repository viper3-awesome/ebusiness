package com.ch.ch16.controller.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.CartEntity;
import com.ch.ch16.entity.GoodsEntity;
import com.ch.ch16.service.admin.GoodsService;
import com.ch.ch16.service.before.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/before/cart")
@SuppressWarnings("all")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @PostMapping("/add")
    public ResponseResult<Map<String, Object>> addCart(@RequestBody CartEntity cartEntity){
        return cartService.addCart(cartEntity);
    }
    //查询我的购物车
    @PostMapping ("/myCart")
    public ResponseResult<List<Map<String, Object>>> myCartGoods(@RequestBody CartEntity cartEntity){
        return goodsService.myCartGoods(cartEntity);
    }
    //批量更新购物车
    @PostMapping("/bupDateCart")
    public ResponseResult<Map<String, Object>> bupDateCart(@RequestBody CartEntity cartEntity){
        return cartService.bupDateCart(cartEntity);
    }
    //清空购物车
    @PostMapping("/clearCart")
    public ResponseResult<Map<String, Object>> clearCart(@RequestBody CartEntity cartEntity){
        return cartService.clearCart(cartEntity);
    }
    //删除购物车
    @PostMapping("/removeCart")
    public ResponseResult<Map<String, Object>> removeCart(@RequestBody CartEntity cartEntity){
        return cartService.removeCart(cartEntity);
    }
}
