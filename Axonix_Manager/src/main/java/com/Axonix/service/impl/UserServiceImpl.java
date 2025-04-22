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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);

        if (users == null || users.isEmpty()) {
            throw new DuplicateUsernameException("用户名不存在");
        }

        User user = users.get(0);
        if (!user.getPassword().equals(password)) {
            throw new DuplicateUsernameException("密码错误");
        }
        return user;
    }

    private void checkUsernameUnique(User user) throws DuplicateUsernameException {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());

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
    public int register(User user, MultipartFile avatarFile) throws DuplicateUsernameException, IOException {
        checkUsernameUnique(user);

        if (avatarFile != null && !avatarFile.isEmpty()) {
            // 相对路径：resources/static/images/
            String relativePath = "src/main/resources/static/images/";
            String savePath = System.getProperty("user.dir") + File.separator + relativePath;

            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
            File dest = new File(savePath + fileName);
            avatarFile.transferTo(dest);

            // 设置数据库保存路径
            user.setAvatar("/images/" + fileName);
        }

        return userMapper.insert(user);
    }


    @Override
    @Transactional
    public int updateByExampleSelective(User record, UserExample example) {
        if (record.getUsername() != null) {
            checkUsernameUnique(record);
        }
        return userMapper.updateByExampleSelective(record, example);
    }

    @Override
    @Transactional
    public int updateByPrimaryKeySelective(User record) {
        if (record.getUsername() != null) {
            checkUsernameUnique(record);
        }
        return userMapper.updateByPrimaryKeySelective(record);
    }


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
    public int updateByPrimaryKey(User user) {
        SimpleDateFormat chinaFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        chinaFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        user.setLastLoginTime(new Date());
        user.setUpdatedAt(new Date());

        if (user.getLastLoginTime() != null) {
            String formatted = chinaFormat.format(user.getLastLoginTime());
            user.setLastLoginTime(parseDate(formatted, chinaFormat)); // 使用修正后的 parseDate
        }

        if (user.getCreatedAt() != null) {
            user.setCreatedAt(parseDate(chinaFormat.format(user.getCreatedAt()), chinaFormat));
        }

        if (user.getUpdatedAt() != null) {
            String updatedFormatted = chinaFormat.format(user.getUpdatedAt());
            user.setUpdatedAt(parseDate(updatedFormatted, chinaFormat));
        }

        return userMapper.updateByPrimaryKey(user);
    }

    private Date parseDate(String dateStr, SimpleDateFormat sdf) {
        try {
            // 设置中国时区（关键修正）
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 或抛出自定义异常
        }
    }

    @Override
    public String updateAvatar(Integer userId, MultipartFile avatarFile) throws IOException {

        if (avatarFile == null || avatarFile.isEmpty()) {
            return "0"; // 没上传文件
        }

        String relativePath = "src/main/resources/static/images/";
        String savePath = System.getProperty("user.dir") + File.separator + relativePath;

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
        File dest = new File(savePath + fileName);
        avatarFile.transferTo(dest);

        // 创建 User 对象并更新数据库中头像字段
        User user = new User();
        user.setId(userId);
        user.setAvatar("/images/" + fileName);
        userMapper.updateByPrimaryKeySelective(user); // 只更新非空字段
        return user.getAvatar();
    }


}