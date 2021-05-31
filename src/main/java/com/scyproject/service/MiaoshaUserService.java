package com.scyproject.service;


import com.scyproject.dao.MiaoshaUserDao;
import com.scyproject.domain.MiaoshaUser;
import com.scyproject.result.CodeMsg;
import com.scyproject.util.MD5Util;
import com.scyproject.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {
	@Autowired
	MiaoshaUserDao miaoshaUserDao;

	public MiaoshaUser getById(Long id){
		return miaoshaUserDao.getById(id);
	}

	public CodeMsg login(LoginVo loginVo){
		if(loginVo == null){
			return CodeMsg.SERVER_ERROR;
		}
		String mobile = loginVo.getMobile();
		String password = loginVo.getPassword();
		MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile ));
		if( miaoshaUser == null ){
			return CodeMsg.MOBILE_NOT_EXIST;
		}
		//验证密码
		String dbPass = miaoshaUser.getPassword();
		String salt = miaoshaUser.getSalt();
		String pass = MD5Util.formToDB(password,salt);
		if(pass.equals(dbPass)){
			return CodeMsg.SUCCESS;
		}else{
			return CodeMsg.PASSWORD_ERROR;
		}
	}

}
