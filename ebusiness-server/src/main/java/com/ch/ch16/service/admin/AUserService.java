package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.AUserEntity;

import java.util.Map;

public interface AUserService {
    ResponseResult<Map<String, String>> login(AUserEntity aUserEntity);
}
