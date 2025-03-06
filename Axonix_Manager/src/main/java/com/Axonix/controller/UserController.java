package com.Axonix.controller;

import com.Axonix.demo.model.User;
import com.Axonix.demo.model.UserExample;
import com.Axonix.service.UserService;
import com.Axonix.util.JwtUtil;  // 确保导入 JwtUtil 类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;  // 确保注入 JwtUtil

    @GetMapping("/count")
    public long countUsers(UserExample example) {
        return userService.countUsers(example);
    }

    @DeleteMapping("/deleteByExample")
    public int deleteUsers(@RequestBody UserExample example) {
        return userService.deleteUsers(example);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteUserById(@PathVariable Integer id) {
        return userService.deleteUserById(id);
    }

    @PostMapping("/add")
    public int addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/addSelective")
    public int addUserSelective(@RequestBody User user) {
        return userService.addUserSelective(user);
    }

    @PostMapping("/getByExampleWithBLOBs")
    public List<User> getUsersByExampleWithBLOBs(@RequestBody UserExample example) {
        return userService.getUsersByExampleWithBLOBs(example);
    }

    @PostMapping("/getByExample")
    public List<User> getUsersByExample(@RequestBody UserExample example) {
        return userService.getUsersByExample(example);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/updateSelective")
    public int updateUsersSelective(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsersSelective(user, example);
    }

    @PutMapping("/updateWithBLOBs")
    public int updateUsersWithBLOBs(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsersWithBLOBs(user, example);
    }

    @PutMapping("/update")
    public int updateUsers(@RequestBody User user, @RequestBody UserExample example) {
        return userService.updateUsers(user, example);
    }

    @PutMapping("/updateByIdSelective")
    public int updateUserByIdSelective(@RequestBody User user) {
        return userService.updateUserByIdSelective(user);
    }

    @PutMapping("/updateByIdWithBLOBs")
    public int updateUserByIdWithBLOBs(@RequestBody User user) {
        return userService.updateUserByIdWithBLOBs(user);
    }

    @PutMapping("/updateById")
    public int updateUserById(@RequestBody User user) {
        return userService.updateUserById(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        UserExample example = new UserExample();
        // 设置查询条件：用户名、密码和角色必须符合要求
        example.createCriteria()
                .andUsernameEqualTo(loginUser.getUsername())
                .andPasswordEqualTo(loginUser.getPassword())
                .andRoleEqualTo("admin");
        List<User> userList = userService.getUsersByExample(example);

        if (userList.isEmpty()) {
            // 没有匹配的用户，则返回错误
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("用户名或密码错误，或者您不是管理员");
        }

        // 登录成功，生成并返回 Token
        String token = jwtUtil.generateToken(userList.get(0).getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", token));  // 返回 token
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");  // 提取 Bearer 后的 token
        String username = jwtUtil.extractUsername(token);  // 从 token 中提取用户名

        // 根据用户名获取用户信息（可以根据需要调整逻辑）
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(Collections.singletonMap("name", user.getUsername()));  // 返回用户名
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 token 或找不到用户");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);  // 从 token 中提取用户名
        boolean isValid = jwtUtil.validateToken(token, username);  // 验证 token 是否有效

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 token");
        }

        // 如果需要，可以在这里执行额外的操作，比如将 token 加入黑名单等
        return ResponseEntity.ok("注销成功");
    }


}
