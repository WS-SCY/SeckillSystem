package com.scyproject.service;

import com.scyproject.dao.UserDao;
import com.scyproject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public Boolean transactionTest(){
        userDao.insert(new User(3,"用户3"));
        userDao.insert(new User(4,"用户4"));
        return true;
    }

}
