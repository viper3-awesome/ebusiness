package com.ch.ch16.common.sercurity.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HttpJwtTokenUtil {
	public static final String KEY_USERNAME = "userName";

	@Autowired
	private ConfigrarionBean jwtConfig;
	@Autowired
	private JwtTokenUtil jwtUtil;

	public boolean validate(HttpServletRequest request) {
		Map<String, String> userDetail = new HashMap<>(2);
		final String requestTokenHeader = request.getHeader(jwtConfig.getTokenHeader());
		if(StrUtil.isEmpty(requestTokenHeader)) {
			return false;
		}
		if(!jwtUtil.verify(requestTokenHeader)) {
			return false;
		}
		return jwtUtil.isExpiration(requestTokenHeader);
	}

	public String getRequestPostStr(HttpServletRequest request) {
		byte buffer[] = getRequestPostBytes(request);
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		try {
			return new String(buffer, charEncoding);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public byte[] getRequestPostBytes(HttpServletRequest request) {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		try {
			for (int i = 0; i < contentLength;) {
				int readlen;
				readlen = request.getInputStream().read(buffer, i, contentLength - i);
				if (readlen == -1) {
					break;
				}
				i += readlen;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return buffer;
	}
}
