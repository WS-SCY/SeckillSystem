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
        int i= 1;
        double j = 1.2345;
        System.out.println(( JSON.toJSONString(j) ).getClass() +"  "+JSON.toJSONString(j));
    }
}
