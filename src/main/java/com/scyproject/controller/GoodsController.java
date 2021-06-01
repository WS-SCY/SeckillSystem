package com.scyproject.controller;


import com.alibaba.druid.util.StringUtils;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.redis.RedisService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	MiaoshaUserService userService;

	@Autowired
	RedisService redisService;

	@RequestMapping("/to_list")
	public String toList(Model model,MiaoshaUser miaoshaUser
	){
		model.addAttribute("user",miaoshaUser);
		return "goods_list";
	}



    
}
