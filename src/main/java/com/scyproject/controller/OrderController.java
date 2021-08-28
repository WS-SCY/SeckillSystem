package com.scyproject.controller;

import com.scyproject.domain.MiaoshaUser;
import com.scyproject.domain.OrderInfo;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.OrderService;
import com.scyproject.vo.GoodsVo;
import com.scyproject.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;

	/**
	 * 查询订单详情页
	 *
	 * */
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info( MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoById(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
