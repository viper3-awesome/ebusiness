package com.ch.ch16.service.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.FocusEntity;

import java.util.Map;

public interface FocusService {
    ResponseResult<Map<String, Object>> focus(FocusEntity focusEntity);
    long countByGoodsId(Integer goodsId);
}
