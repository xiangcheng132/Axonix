package com.Axonix.controller;

import com.Axonix.demo.model.Admin;
import com.Axonix.demo.model.AdminExample;
import com.Axonix.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    private static final Map<String, Admin> loggedInAdmins = new HashMap<>();
    // 统计数量
    @PostMapping("/count")
    public long countByExample(@RequestBody AdminExample example) {
        return adminService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody AdminExample example) {
        return adminService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return adminService.deleteByPrimaryKey(id);
    }

    // 完整插入
    @PostMapping("/insert")
    public int insert(@RequestBody Admin record) {
        return adminService.insert(record);
    }

    // 选择性插入
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody Admin record) {
        return adminService.insertSelective(record);
    }

    // 条件查询
    @PostMapping("/select/by-example")
    public List<Admin> selectByExample(@RequestBody AdminExample example) {
        return adminService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public Admin selectByPrimaryKey(@PathVariable Integer id) {
        return adminService.selectByPrimaryKey(id);
    }

    // 条件选择性更新
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody Admin record,
            @RequestBody AdminExample example) {
        return adminService.updateByExampleSelective(record, example);
    }

    // 条件完整更新
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody Admin record,
            @RequestBody AdminExample example) {
        return adminService.updateByExample(record, example);
    }

    // 主键选择性更新
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody Admin record) {
        return adminService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody Admin record) {
        return adminService.updateByPrimaryKey(record);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(admin.getUsername());
        criteria.andPasswordEqualTo(admin.getPassword());

        List<Admin> admins = adminService.selectByExample(example);
        if (admins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // 生成 token
        String token = UUID.randomUUID().toString();
        loggedInAdmins.put(token, admins.get(0)); // 记录已登录的用户

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
        }

        String token = authHeader.substring(7);
        Admin admin = loggedInAdmins.get(token);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return ResponseEntity.ok(admin);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
        }

        String token = authHeader.substring(7);
        loggedInAdmins.remove(token); // 移除 token，用户登出
        return ResponseEntity.ok("Logout successful");
    }

}
