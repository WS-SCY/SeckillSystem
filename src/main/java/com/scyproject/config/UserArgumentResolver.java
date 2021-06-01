package com.scyproject.config;

import com.scyproject.domain.MiaoshaUser;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.UserService;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	MiaoshaUserService userService;

	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz== MiaoshaUser.class;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class );
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		String paramToken = request.getParameter(MiaoshaUserService.COOKI_TOKEN_NAME);
		String cookieToken = getCookieValue(request,MiaoshaUserService.COOKI_TOKEN_NAME);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiTokenName) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(cookiTokenName)){
				return cookie.getValue();
			}
		}
		return null;
	}

}
