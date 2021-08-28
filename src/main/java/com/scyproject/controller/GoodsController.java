package com.scyproject.controller;


import com.scyproject.domain.MiaoshaUser;
import com.scyproject.redis.GoodsKey;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaUserService;
import com.scyproject.service.UserService;
import com.scyproject.vo.GoodsDetailVo;
import com.scyproject.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * 跳转到list页面
	 *
	 * */
	@RequestMapping(value="/to_list", produces="text/html")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
		model.addAttribute("user", user);
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsList, "all", String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		//取不到就读数据库
		System.out.println("读了数据库");
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);

		//手动渲染
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "all", html);
		}
		return html;
	}


	/**
	 * 商品详情页
	 *
	 * */
	@RequestMapping(value="/detail/{goodsId}")
	@ResponseBody
	public Result detail(MiaoshaUser user,
										@PathVariable("goodsId")long goodsId) {
		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		GoodsVo goods = goodsService.getGoodsVoById(goodsId);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int miaoshaStatus = 0;
		int remainSeconds = 0;
		if(now < startAt ) {//秒杀还没开始，倒计时
			miaoshaStatus = 0;
			remainSeconds = (int)((startAt - now )/1000);
		}else if(now > endAt){//秒杀已经结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else{//秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setRemainSeconds(remainSeconds);
		vo.setMiaoshaStatus(miaoshaStatus);
		return Result.success(vo);
	}

}
