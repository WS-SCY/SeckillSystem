package com.scyproject.controller;


import com.scyproject.domain.MiaoshaOrder;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.domain.OrderInfo;
import com.scyproject.redis.GoodsKey;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.OrderService;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController  {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MiaoshaService miaoshaService;

	@RequestMapping("/do_miaosha")
	public String list(Model model,MiaoshaUser user,@RequestParam("goodsId")long goodsId){
		model.addAttribute("user",user);
		if(user == null){
			return "login";
		}
		GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
		int stock = goodsVo.getStockCount();
		if(stock<=0){
			model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}
		//判断是否秒杀到了
		MiaoshaOrder order =  orderService.getOrderByUserAndGoods(user.getId(),goodsId);
		if(order != null){
			model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
			return "miaosha_fail";
		}
		//减库存 下订单 写入秒杀订单（事务）
		OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
		model.addAttribute("orderInfo",orderInfo);
		model.addAttribute("goods",goodsVo);
		return "order_detail";
	}

}
