package com.ch.ch16.controller.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.sercurity.anno.AuthIgrone;
import com.ch.ch16.entity.AUserEntity;
import com.ch.ch16.service.admin.AUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@SuppressWarnings("all")
public class AUserController {
    @Autowired
    private AUserService aUserService;
    @AuthIgrone
    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody AUserEntity aUserEntity){
       return aUserService.login(aUserEntity);
    }
}
