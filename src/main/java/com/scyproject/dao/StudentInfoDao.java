package com.scyproject.dao;

import com.scyproject.domain.StudentInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;


@Mapper
public interface StudentInfoDao {
    StudentInfo selectById(@Param("id") Integer id);
}
