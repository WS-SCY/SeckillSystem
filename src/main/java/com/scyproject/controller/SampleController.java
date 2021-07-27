package com.scyproject.controller;

import com.scyproject.domain.User;
import com.scyproject.redis.RedisService;
import com.scyproject.redis.UserKey;
import com.scyproject.result.CodeMsg;
import com.scyproject.result.Result;
import com.scyproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;



    @GetMapping("/success")
    @ResponseBody
    public Result<String> test0(){
        System.out.println(0);
        return Result.success("hi");
    }



    @GetMapping("/error")
    @ResponseBody
    public Result<String> test1(){
        System.out.println(1);
        return Result.error(CodeMsg.SERVER_ERROR);
    }



    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        System.out.println("o");
        model.addAttribute("name","scy");
        return "hello";
    }


//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public String getredis(){
//        return redisService.get("ha",String.class);
//    }
//
//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public Result<Boolean> setredis(){
//        Boolean res = redisService.set("ws","scy");
//        return Result.success(res);
//    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("11111");
        redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }

}
