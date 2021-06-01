package com.scyproject;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

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


    @Test
    public void hi()
    {
        String i = "111";
        System.out.println(String.format("222%s",i));
//        System.out.println(( JSON.toJSONString(j) ).getClass() +"  "+JSON.toJSONString(j));
    }
}
