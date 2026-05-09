package com.ch.ch16.common.sercurity.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigrarionBean {
    @Value("${token.header}")
    private String tokenHeader;
    @Value("${token.secret}")
    private String secret;
    @Value("${token.iss}")
    private String iss;
    @Value("${token.expiration}")
    private int expiration;
    @Value("${redis.expiration}")
    private long redisExpiration;
}
