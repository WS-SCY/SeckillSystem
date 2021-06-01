package com.scyproject.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.UserService;
import com.scyproject.util.ValidatorUtil;
import com.scyproject.vo.LoginVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
//	@Autowired
//    UserService userService;

    @Autowired
    MiaoshaUserService userService;

	@Autowired
    RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,@Valid LoginVo loginVo) {
        userService.login(response ,loginVo);
        return Result.success(true);
    }
}
