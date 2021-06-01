package com.scyproject.dao;


import com.scyproject.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoshaUserDao {
	@Select("select * from secKillUser where id = #{id}")
	public MiaoshaUser getById(@Param("id")long id);
}
