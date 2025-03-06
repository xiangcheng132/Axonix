package com.Axonix.service;

import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;

import java.util.List;

public interface UserService {
    long countUsers(UserExample example);

    int deleteUsers(UserExample example);

    int deleteUserById(Integer id);

    int addUser(User user);

    int addUserSelective(User user);

    List<User> getUsersByExampleWithBLOBs(UserExample example);

    List<User> getUsersByExample(UserExample example);

    User getUserById(Integer id);

    int updateUsersSelective(User user, UserExample example);

    int updateUsersWithBLOBs(User user, UserExample example);

    int updateUsers(User user, UserExample example);

    int updateUserByIdSelective(User user);

    int updateUserByIdWithBLOBs(User user);

    int updateUserById(User user);

    User getUserByUsername(String username);
}
