package com.scyproject.dao;


import com.scyproject.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MiaoshaUserDao {
	@Select("select * from user where id = #{id}")
	public MiaoshaUser getById(@Param("id")long id);

	@Update("update user set password = #{password} where id = #{id}")
	public void update(MiaoshaUser toBeUpdate);
}
