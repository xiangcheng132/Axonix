package com.Axonix.controller;

import com.Axonix.demo.model.Admin;
import com.Axonix.demo.model.AdminExample;
import com.Axonix.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
}