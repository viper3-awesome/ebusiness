package com.ch.ch16.service.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.BUserEntity;

import java.util.Map;

public interface BUserService {
    ResponseResult<Map<String, String>> register(BUserEntity bUserEntity);
    ResponseResult<Map<String, String>> login(BUserEntity bUserEntity);
}
