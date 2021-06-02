package com.scyproject.controller;


import com.alibaba.druid.util.StringUtils;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.redis.RedisService;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.UserService;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	MiaoshaUserService userService;

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@RequestMapping("/to_list")
	public String toList(Model model,MiaoshaUser miaoshaUser
	){
		model.addAttribute("user",miaoshaUser);
		List<GoodsVo> list = goodsService.listGoodsVo();
		model.addAttribute("goodsList",list);
		return "goods_list";
	}
    @RequestMapping("/to_detail/{id}")
	public String detail(Model model, MiaoshaUser user,
						 @PathVariable("id")long id){
		model.addAttribute("user",user);
		GoodsVo gv = goodsService.getGoodsVoById(id);
		model.addAttribute("goods",gv);

		long startAt = gv.getStartDate().getTime();
		System.out.println("goods = "+gv);
		long endAt = gv.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int  status,reaminSeconds;
		if(now < startAt){ //来早了
			status = 0;
			reaminSeconds = (int) ((startAt - now)/1000);
		}else if(now > endAt){  //来晚了
			status = 1;
			reaminSeconds = -1;
		}else{
			status = 2;
			reaminSeconds = 0;
		}
		model.addAttribute("status",status);
		model.addAttribute("remainSeconds",reaminSeconds);
		return "goods_detail";
	}
}
