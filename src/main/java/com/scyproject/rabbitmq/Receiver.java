package com.scyproject.rabbitmq;

import com.scyproject.domain.MiaoshaOrder;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.redis.RedisService;
import com.scyproject.service.GoodsService;
import com.scyproject.service.MiaoshaService;
import com.scyproject.service.OrderService;
import com.scyproject.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: SunCY
 * @Description: TODO
 * @DateTime: 2021/8/3 22:10
 **/
@Service
public class Receiver {
    public static Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues=MQConfig.Queue1)
    public void receive(String message) {
        logger.info("receive message:"+message);
        Message mm  = RedisService.stringToBean(message, Message.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        if(goods == null)
            return ;
        Integer stock = goods.getStockCount();
        if(stock == null || stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getOrderByUserAndGoods(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }
}
