package com.Axonix.service.impl;

import com.Axonix.demo.mapper.UserMapper;
import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.exception.DuplicateUsernameException;
import com.Axonix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 检查用户名是否已存在
    private void checkUsernameUnique(User user) throws DuplicateUsernameException {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());

        // 如果是更新操作，需要排除当前用户自身
        if (user.getId() != null) {
            criteria.andIdNotEqualTo(user.getId());
        }

        long count = userMapper.countByExample(example);
        if (count > 0) {
            throw new DuplicateUsernameException("用户名已存在");
        }
    }

    @Override
    @Transactional
    public int insert(User user) throws DuplicateUsernameException {
        checkUsernameUnique(user);
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public int insertSelective(User user) throws DuplicateUsernameException {
        checkUsernameUnique(user);
        return userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public int updateByExampleSelective(User record, UserExample example) {
        // 如果修改了用户名，需要校验
        if (record.getUsername() != null) {
            checkUsernameUnique(record);
        }
        return userMapper.updateByExampleSelective(record, example);
    }

    @Override
    @Transactional
    public int updateByPrimaryKeySelective(User record) {
        // 如果修改了用户名，需要校验
        if (record.getUsername() != null) {
            checkUsernameUnique(record);
        }
        return userMapper.updateByPrimaryKeySelective(record);
    }

    // 以下方法保持原有实现，省略其他方法...

    @Override
    public long countByExample(UserExample example) {
        return userMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(UserExample example) {
        return userMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<User> selectByExample(UserExample example) {
        return userMapper.selectByExample(example);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExample(User record, UserExample example) {
        return userMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }
}