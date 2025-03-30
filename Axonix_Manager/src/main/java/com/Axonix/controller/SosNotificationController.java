package com.Axonix.controller;

import com.Axonix.demo.model.SosNotification;
import com.Axonix.demo.model.SosNotificationExample;
import com.Axonix.service.SosNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sos-notifications")
public class SosNotificationController {

    @Autowired
    private SosNotificationService sosNotificationService;

    // 统计SOS记录数量
    @PostMapping("/count")
    public long countByExample(@RequestBody SosNotificationExample example) {
        return sosNotificationService.countByExample(example);
    }

    // 条件删除SOS记录
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody SosNotificationExample example) {
        return sosNotificationService.deleteByExample(example);
    }

    // 主键删除SOS记录
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return sosNotificationService.deleteByPrimaryKey(id);
    }

    // 完整插入SOS记录（含BLOB内容）
    @PostMapping("/insert")
    public int insert(@RequestBody SosNotification record) {
        return sosNotificationService.insert(record);
    }

    // 选择性插入SOS记录（仅非空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody SosNotification record) {
        return sosNotificationService.insertSelective(record);
    }

    // 条件查询SOS记录（带BLOB内容）
    @PostMapping("/select/by-example-with-blobs")
    public List<SosNotification> selectByExampleWithBLOBs(@RequestBody SosNotificationExample example) {
        return sosNotificationService.selectByExampleWithBLOBs(example);
    }

    // 条件查询SOS记录（基础字段）
    @PostMapping("/select/by-example")
    public List<SosNotification> selectByExample(@RequestBody SosNotificationExample example) {
        return sosNotificationService.selectByExample(example);
    }

    // 主键查询SOS记录详情
    @GetMapping("/select/{id}")
    public SosNotification selectByPrimaryKey(@PathVariable Integer id) {
        return sosNotificationService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody SosNotification record,
            @RequestBody SosNotificationExample example) {
        return sosNotificationService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（含BLOB内容）
    @PutMapping("/update/by-example/with-blobs")
    public int updateByExampleWithBLOBs(
            @RequestBody SosNotification record,
            @RequestBody SosNotificationExample example) {
        return sosNotificationService.updateByExampleWithBLOBs(record, example);
    }

    // 条件完整更新（基础字段）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody SosNotification record,
            @RequestBody SosNotificationExample example) {
        return sosNotificationService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody SosNotification record) {
        return sosNotificationService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（含BLOB内容）
    @PutMapping("/update/with-blobs")
    public int updateByPrimaryKeyWithBLOBs(@RequestBody SosNotification record) {
        return sosNotificationService.updateByPrimaryKeyWithBLOBs(record);
    }

    // 主键完整更新（基础字段）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody SosNotification record) {
        return sosNotificationService.updateByPrimaryKey(record);
    }
}