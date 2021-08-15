package com.scyproject.rabbitmq;

import com.scyproject.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: SunCY
 * @Description: TODO
 * @DateTime: 2021/8/3 22:10
 **/
@Service
public class Sender {
    public static Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Message msg){
        String msgstr = RedisService.beanToString(msg);
        logger.info("send msg = "+msg);
        amqpTemplate.convertAndSend(MQConfig.Queue1,msgstr);
    }
}
