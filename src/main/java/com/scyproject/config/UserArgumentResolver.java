package com.scyproject.config;
import com.scyproject.access.UserContext;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
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
		return UserContext.getUser();
	}

	private String getCookieValue(HttpServletRequest request, String cookiTokenName) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(cookiTokenName)){
				return cookie.getValue();
			}
		}
		return null;
	}

}
