package com.scyproject.service;

import com.scyproject.dao.GoodsDao;
import com.scyproject.domain.Goods;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.domain.OrderInfo;
import com.scyproject.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods){
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);

        OrderInfo orderInfo = orderService.createOrder(user,goods);

        return orderInfo;
    }
}
