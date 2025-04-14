package com.Axonix.controller;

import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 统计用户数量
    @PostMapping("/count")
    public long countByExample(@RequestBody UserExample example) {
        return userService.countByExample(example);
    }

    // 条件删除用户
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody UserExample example) {
        return userService.deleteByExample(example);
    }

    // 主键删除用户
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return userService.deleteByPrimaryKey(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }
    // 完整注册用户（全字段）
    @PostMapping("/register")
    public int register(@RequestBody User user) {
        return userService.insert(user);
    }

    // 选择性注册用户（仅必填字段）
    @PostMapping("/register/selective")
    public int registerSelective(@RequestBody User user) {
        return userService.insertSelective(user);
    }

    // 条件查询用户列表
    @PostMapping("/list")
    public List<User> selectByExample(@RequestBody UserExample example) { return userService.selectByExample(example); }

    // 主键查询用户详情
    @GetMapping("/detail/{id}")
    public User selectByPrimaryKey(@PathVariable Integer id) {
        return userService.selectByPrimaryKey(id);
    }

    // 条件选择性更新用户信息
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody User record,
            @RequestBody UserExample example) {
        return userService.updateByExampleSelective(record, example);
    }

    // 条件完整更新用户信息
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody User record,
            @RequestBody UserExample example) {
        return userService.updateByExample(record, example);
    }

    // 主键选择性更新用户信息
    @PutMapping("/update/selective")
    public int updateProfile(@RequestBody User user) {
        return userService.updateByPrimaryKeySelective(user);
    }

    // 主键完整更新用户信息
    @PutMapping("/update")
    public int updateUser(@RequestBody User user) {
        return userService.updateByPrimaryKey(user);
    }

}