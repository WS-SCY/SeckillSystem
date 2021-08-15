package com.scyproject.controller;

import com.scyproject.redis.GoodsKey;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.GoodsService;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: SunCY
 * @Description: 管理员，同步redis数据库
 * @DateTime: 2021/8/4 0:30
 **/

@Controller
@RequestMapping("/operator")
public class OperatorController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/synchronization")
    @ResponseBody
    public Result operatorSynchronization(){
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return Result.error(CodeMsg.GOODS_LIST_EMPTY);
        }
        for(GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), goods.getStockCount());
            MiaoshaController.localOverMap.put(goods.getId(), false);
        }
        return Result.success("同步成功");
    }
}
