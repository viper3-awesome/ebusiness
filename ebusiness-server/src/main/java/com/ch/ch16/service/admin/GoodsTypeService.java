package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.GoodsTypeEntity;

import java.util.List;
import java.util.Map;

public interface GoodsTypeService {
    ResponseResult<Map<String, Object>> getGoodsType(GoodsTypeEntity goodsTypeEntity);
    ResponseResult<Map<String, Object>> add(GoodsTypeEntity goodsTypeEntity);
    ResponseResult<Map<String, Object>> update(GoodsTypeEntity goodsTypeEntity);
    ResponseResult<Map<String, Object>> delete(GoodsTypeEntity goodsTypeEntity);
    List<GoodsTypeEntity> list();
}
