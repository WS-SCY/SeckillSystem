package com.scyproject.service;


import com.scyproject.dao.MiaoshaUserDao;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.exception.GlobalException;
import com.scyproject.redis.MiaoshaUserKey;
import com.scyproject.redis.RedisService;
import com.scyproject.result.CodeMsg;
import com.scyproject.util.MD5Util;
import com.scyproject.util.UUIDUtil;
import com.scyproject.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class MiaoshaUserService {
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;

	public static final String COOKI_TOKEN_NAME = "name";

	public MiaoshaUser getById(Long id){
		return miaoshaUserDao.getById(id);
	}

	public MiaoshaUser getByToken(HttpServletResponse response,String token){
	    if(StringUtils.isEmpty(token)){
	        return null;
        }
	    MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
	    //延长有效期
        if(user != null){
            addCookie(response,token,user);
        }
        return user;
    }

	public boolean login(HttpServletResponse response,  LoginVo loginVo){
		if(loginVo == null){
			throw new GlobalException( CodeMsg.SERVER_ERROR );
		}
		String mobile = loginVo.getMobile();
		String password = loginVo.getPassword();
		MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile ));
		if( miaoshaUser == null ){
			throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST );
		}
		//验证密码
		String dbPass = miaoshaUser.getPassword();
		String salt = miaoshaUser.getSalt();
		String pass = MD5Util.formToDB(password,salt);
		if(!pass.equals(dbPass)){
			throw new GlobalException( CodeMsg.PASSWORD_ERROR );
		}
		//生成cookie
		String token = UUIDUtil.uuid();
        addCookie(response,token,miaoshaUser);
		return true;
	}
	private void  addCookie(HttpServletResponse response,String token,MiaoshaUser miaoshaUser){
        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
        Cookie cookie = new Cookie(COOKI_TOKEN_NAME,token);
        cookie.setMaxAge(MiaoshaUserKey.token.getExpireSeconds());
        cookie.setPath("/"); //设置根目录
        response.addCookie(cookie);
    }

}
