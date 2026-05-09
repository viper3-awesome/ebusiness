package com.ch.ch16.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value={"classpath:conf/conf.properties"})
public class PropertyConfig {

}
