package com.scyproject.redis;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(String key,Class<T> myclass){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            T t = stringToBean(str,myclass);
            return t;
        }finally{
            returnToPool(jedis);
        }
    }

    public <T> Boolean set(String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length()<0){
                return false;
            }
            jedis.set(key,str);
            return true;
        }finally{
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value){
        if(value == null){
            return null;
        }
        Class<?> myclass =  value.getClass();
        if(myclass == int.class || myclass == Integer.class){
            return ""+value;
        }else if(myclass == long.class || myclass == Long.class){
            return ""+value;
        }else if(myclass == String.class){
            return (String)value;
        }
        return JSON.toJSONString(value);
    }

    private<T> T stringToBean(String str,Class<T> myclass){
        if(str == null || str.length()<0 || myclass == null)
            return null;
        if(myclass == int.class || myclass == Integer.class){
            return (T)Integer.valueOf(str);
        }else if(myclass == long.class || myclass == Long.class){
            return (T)Long.valueOf(str);
        }else if(myclass == String.class){
            return (T)str;
        }
        return JSON.toJavaObject(JSON.parseObject(str),myclass);
    }
    private void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }


}
