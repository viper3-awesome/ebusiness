package com.ch.ch16.controller.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.GoodsTypeEntity;
import com.ch.ch16.entity.OrdersEntity;
import com.ch.ch16.service.admin.OrderdetailServiceImpl;
import com.ch.ch16.service.admin.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@SuppressWarnings("all")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/getAllOrders")
    public ResponseResult<Map<String, Object>> getAllOrders(@RequestBody OrdersEntity ordersEntity){
        return ordersService.getAllOrders(ordersEntity);
    }
    @PostMapping("/getOrdersDetail")
    public ResponseResult<List<Map<String, Object>>> getOrdersDetail(@RequestBody OrdersEntity ordersEntity){
        return ordersService.getOrdersDetail(ordersEntity);
    }
    @PostMapping("/goPay")
    public ResponseResult<Map<String, String>> goPay(@RequestBody OrdersEntity ordersEntity){
        return ordersService.goPay(ordersEntity);
    }
    @PostMapping("/selectOrderByMonth")
    public ResponseResult<List<OrdersEntity>> selectOrderByMonth(@RequestBody OrdersEntity ordersEntity){
        return ordersService.selectOrderByMonth(ordersEntity);
    }
    @PostMapping("/selectOrderByType")
    public ResponseResult<List<Map<String, Object>>> selectOrderByType(){
        return ordersService.selectOrderByType();
    }
    @PostMapping("/submitOrder")
    public ResponseResult<OrdersEntity> submitOrder(@RequestBody OrdersEntity ordersEntity){
        return ordersService.submitOrder(ordersEntity);
    }
    @PostMapping("/goSubmitOrder")
    public ResponseResult<OrdersEntity> goSubmitOrder(@RequestBody OrdersEntity ordersEntity){
        return ordersService.goSubmitOrder(ordersEntity);
    }
    @PostMapping("/getOrdersByUid")
    public ResponseResult<Map<String, Object>> getOrdersByUid(@RequestBody OrdersEntity ordersEntity){
        return ordersService.getOrdersByUid(ordersEntity);
    }
}
