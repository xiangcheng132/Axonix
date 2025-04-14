package com.Axonix.service;

import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.exception.DuplicateUsernameException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(User record, UserExample example);

    int updateByExample(User record, UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(String username, String password);

    int register(User user, MultipartFile avatarFile) throws DuplicateUsernameException, IOException;

    int updateAvatar(Integer userId, MultipartFile avatarFile) throws IOException;

}