package com.Axonix.service.impl;

import com.Axonix.demo.mapper.UserMapper;
import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

//    @PostConstruct
//    public void testMapper() {
//        System.out.println("UserMapper Bean: " + userMapper);
//    }

    @Override
    public long countUsers(UserExample example) {
        return userMapper.countByExample(example);
    }

    @Override
    @Transactional
    public int deleteUsers(UserExample example) {
        return userMapper.deleteByExample(example);
    }

    @Override
    @Transactional
    public int deleteUserById(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public int addUserSelective(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public List<User> getUsersByExampleWithBLOBs(UserExample example) {
        return userMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<User> getUsersByExample(UserExample example) {
        return userMapper.selectByExample(example);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateUsersSelective(User user, UserExample example) {
        return userMapper.updateByExampleSelective(user, example);
    }

    @Override
    @Transactional
    public int updateUsersWithBLOBs(User user, UserExample example) {
        return userMapper.updateByExampleWithBLOBs(user, example);
    }

    @Override
    @Transactional
    public int updateUsers(User user, UserExample example) {
        return userMapper.updateByExample(user, example);
    }

    @Override
    @Transactional
    public int updateUserByIdSelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public int updateUserByIdWithBLOBs(User user) {
        return userMapper.updateByPrimaryKeyWithBLOBs(user);
    }

    @Override
    @Transactional
    public int updateUserById(User user) {
        return userMapper.updateByPrimaryKey(user);
    }
}
