package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.CartEntity;
import com.ch.ch16.entity.GoodsEntity;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    ResponseResult<Map<String, Object>> getGoods(GoodsEntity goodsEntity);
    ResponseResult<Map<String, Object>> iPageMyFocusGoods(GoodsEntity goodsEntity);
    ResponseResult<List<Map<String, Object>>> myCartGoods(CartEntity cartEntity);
    ResponseResult<List<GoodsEntity>> getAdvGoods();
    ResponseResult<Map<String, String>> add(GoodsEntity goodsEntity);
    ResponseResult<Map<String, String>> delete(GoodsEntity goodsEntity);
    ResponseResult<List<GoodsEntity>> getGoodsIndex(GoodsEntity goodsEntity);
    ResponseResult<GoodsEntity> getGoodsById(GoodsEntity goodsEntity);
    long countByGoodsTypeId(Integer goodsTypeId);
    GoodsEntity getById(Integer id);
    void updateById(GoodsEntity entity);
    void updateBatchById(List<GoodsEntity> entities);
}
