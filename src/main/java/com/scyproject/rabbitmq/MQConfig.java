package com.scyproject.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: SunCY
 * @Description: TODO
 * @DateTime: 2021/8/3 22:10
 **/
@Configuration
public class MQConfig {
    public static final String  Queue1 = "queue1";
    @Bean
    public Queue queue(){
        return new Queue(Queue1,true);
    }
}
