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

	@PostMapping("/do_miaosha")
	@ResponseBody
	public Result Seckill(Model model,MiaoshaUser user,@RequestParam("goodsId")long goodsId){
		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
		int stock = goodsVo.getStockCount();
		if(stock<=0){
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//判断是否秒杀到了
		MiaoshaOrder order =  orderService.getOrderByUserAndGoods(user.getId(),goodsId);
		if(order != null){
			return Result.error(CodeMsg.MIAOSHA_FAIL);
		}
		//减库存 下订单 写入秒杀订单（事务）
		OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
		HashMap<String,Object> hashMap = new HashMap<>();
		hashMap.put("goodsVo",goodsVo);
		hashMap.put("order",order);
		hashMap.put("orderInfo",orderInfo);
		return Result.success(hashMap);
	}

	@RequestMapping("/result")
	@ResponseBody
	public  Result Miaohsa(
			MiaoshaUser user,
			@RequestParam("goodsId")String goodsId
	){
		return Result.success(0,"success",1);
	}

//	@RequestMapping(value="/do_miaosha", method=RequestMethod.POST)
//	@ResponseBody
//	public Result<Integer> miaosha(Model model,MiaoshaUser user,
//								   @RequestParam("goodsId")long goodsId) {
//		if(user == null) {
//			return Result.error(CodeMsg.SESSION_ERROR);
//		}
//		//内存标记，减少redis访问
//		boolean over = localOverMap.get(goodsId);
//		if(over) {
//			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		}
//		//预减库存
//		long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);//10
//		if(stock < 0) {
//			localOverMap.put(goodsId, true);
//			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		}
//		//判断是否已经秒杀到了
//		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//		if(order != null) {
//			return Result.error(CodeMsg.REPEATE_MIAOSHA);
//		}
//		//入队
//		MiaoshaMessage mm = new MiaoshaMessage();
//		mm.setUser(user);
//		mm.setGoodsId(goodsId);
//		sender.sendMiaoshaMessage(mm);
//		return Result.success(0);//排队中
//	}


}
