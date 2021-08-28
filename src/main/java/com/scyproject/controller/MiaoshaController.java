package com.scyproject.controller;


import com.scyproject.access.AccessLimit;
import com.scyproject.domain.MiaoshaOrder;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.domain.OrderInfo;
import com.scyproject.rabbitmq.Message;
import com.scyproject.rabbitmq.Sender;
import com.scyproject.redis.GoodsKey;
import com.scyproject.redis.MiaoshaKey;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.OrderService;
import com.scyproject.util.MD5Util;
import com.scyproject.util.UUIDUtil;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController  {

	public static HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	Sender sender;

	@Autowired
	MiaoshaService miaoshaService;

	@AccessLimit(seconds = 3,maxCount = 3,needLogin = true)
	@RequestMapping(value="/getPath", method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
										 @RequestParam("goodsId")long goodsId,
										 @RequestParam(value="verifyCode", defaultValue="0")int verifyCode) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
		if(!check) {
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		String md5Str = MD5Util.md5(UUIDUtil.uuid() +"123456");
		redisService.set(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,md5Str);
		return Result.success(md5Str);
	}

	/**
	 * 查询秒杀结果
	 *
	 * orderId：成功
	 * -1：秒杀失败
	 * 0： 排队中
	 * */
	@RequestMapping(value="/result", method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
									  @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result  =miaoshaService.getMiaoshaResult(user.getId(), goodsId);
		return Result.success(result);
	}


	/**
	 * 开始秒杀
	 *
	 * return 0 排队中
	 * */
	@RequestMapping(value="/{pathParam}/do_miaosha", method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> miaosha(Model model,MiaoshaUser user,
								   @RequestParam("goodsId")Long goodsId,
								   @PathVariable("pathParam") String pathParam ) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//验证path
		boolean check = miaoshaService.checkPath(user, goodsId, pathParam);
		if(!check){
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}

		//内存标记，减少redis访问
		Boolean over = localOverMap.get(goodsId);
		if(over!=null && over) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//预减库存
		long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);//10
		if(stock < 0) {
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//判断是否曾经秒杀到了
		MiaoshaOrder order = orderService.getOrderByUserAndGoods(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//入队
		Message mm = new Message(user,goodsId);
		sender.send(mm);
		return Result.success(0);
	}

	@RequestMapping(value="/verifyCode", method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaVerifyCod(HttpServletResponse response,MiaoshaUser user,
											  @RequestParam("goodsId")long goodsId) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		try {
			BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
			OutputStream out = response.getOutputStream();
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
			return Result.success("获取验证码成功");
		}catch(Exception e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		}
	}
}
