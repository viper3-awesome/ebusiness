package com.ch.ch16.common.config;

import com.ch.ch16.common.sercurity.intercept.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private AuthInterceptor authInter;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInter).addPathPatterns("/api/**");
	}
}
