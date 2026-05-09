package com.ch.ch16.controller.before;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.sercurity.anno.AuthIgrone;
import com.ch.ch16.common.sercurity.utils.ConfigrarionBean;
import com.ch.ch16.common.sercurity.utils.RedisUtil;
import com.ch.ch16.entity.BUserEntity;
import com.ch.ch16.service.before.BUserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/before")
@SuppressWarnings("all")
public class BUserController {
    @Autowired
    private BUserService bUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConfigrarionBean config;
    @AuthIgrone
    @PostMapping("/register")
    public ResponseResult<Map<String, String>> register(@RequestBody BUserEntity bUserEntity){
        return bUserService.register(bUserEntity);
    }
    @AuthIgrone
    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody BUserEntity bUserEntity){
        return bUserService.login(bUserEntity);
    }
    @AuthIgrone
    @GetMapping("/getcode")
    public void getcode(HttpServletResponse response) throws IOException {
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(116, 30, 4, 10);
        redisUtil.set("code",circleCaptcha.getCode(), config.getRedisExpiration());//验证码存到redis缓存中
        ServletOutputStream outputStream = response.getOutputStream();
        circleCaptcha.write(outputStream);
        outputStream.close();
    }
}
