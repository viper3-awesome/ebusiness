package com.ch.ch16.service.admin;

import com.ch.ch16.entity.OrderdetailEntity;

import java.util.List;

@SuppressWarnings("all")
public interface OrderdetailService {
    OrderdetailEntity save(OrderdetailEntity entity);
    List<OrderdetailEntity> saveBatch(List<OrderdetailEntity> entities);
    long count(OrderdetailEntity example);
}
