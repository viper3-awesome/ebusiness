package com.ch.ch16.common.sercurity.intercept;

import cn.hutool.json.JSONUtil;
import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.http.StatusCode;
import com.ch.ch16.common.sercurity.anno.AuthIgrone;
import com.ch.ch16.common.sercurity.utils.HttpJwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Component
@Slf4j
@SuppressWarnings("all")
public class AuthInterceptor implements HandlerInterceptor {
	@Autowired
	private HttpJwtTokenUtil jwtHttpUtil;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info(request.getRequestURI() + " is start");
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Annotation authIgrone =  handlerMethod.getMethodAnnotation(AuthIgrone.class);
		//无需签名
        if(authIgrone != null) {
        	return true;
        }
        
        boolean res = jwtHttpUtil.validate(request);
		if(!res) {
			ResponseResult<Object> rrs = ResponseResult.getMessageResult(null,"E001", StatusCode.C405);
			response.getWriter().write(JSONUtil.toJsonStr(rrs));
			return false;
		} else {
			return true;
		}
	}
}
