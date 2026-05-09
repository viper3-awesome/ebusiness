package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.OrdersEntity;

import java.util.List;
import java.util.Map;

public interface OrdersService {
    ResponseResult<Map<String, Object>> getAllOrders(OrdersEntity ordersEntity);
    ResponseResult<Map<String, Object>> getOrdersByUid(OrdersEntity ordersEntity);
    ResponseResult<List<Map<String, Object>>> getOrdersDetail(OrdersEntity ordersEntity);
    ResponseResult<Map<String, String>> goPay(OrdersEntity ordersEntity);
    ResponseResult<List<OrdersEntity>> selectOrderByMonth(OrdersEntity ordersEntity);
    ResponseResult<List<Map<String, Object>>> selectOrderByType();
    ResponseResult<OrdersEntity> submitOrder(OrdersEntity ordersEntity);
    ResponseResult<OrdersEntity> goSubmitOrder(OrdersEntity ordersEntity);
}
