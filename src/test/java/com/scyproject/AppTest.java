package com.scyproject;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.rabbitmq.Message;
import com.scyproject.rabbitmq.Sender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Autowired
    Sender sender;

}
