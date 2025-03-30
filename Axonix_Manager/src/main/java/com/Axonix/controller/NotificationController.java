package com.Axonix.controller;

import com.Axonix.demo.model.Notification;
import com.Axonix.demo.model.NotificationExample;
import com.Axonix.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 统计通知数量
    @PostMapping("/count")
    public long countByExample(@RequestBody NotificationExample example) {
        return notificationService.countByExample(example);
    }

    // 条件删除通知
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody NotificationExample example) {
        return notificationService.deleteByExample(example);
    }

    // 主键删除通知
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return notificationService.deleteByPrimaryKey(id);
    }

    // 完整插入通知（含BLOB内容）
    @PostMapping("/insert")
    public int insert(@RequestBody Notification record) {
        return notificationService.insert(record);
    }

    // 选择性插入通知（仅非空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody Notification record) {
        return notificationService.insertSelective(record);
    }

    // 条件查询通知（带BLOB内容）
    @PostMapping("/select/by-example-with-blobs")
    public List<Notification> selectByExampleWithBLOBs(@RequestBody NotificationExample example) {
        return notificationService.selectByExampleWithBLOBs(example);
    }

    // 条件查询通知（基础字段）
    @PostMapping("/select/by-example")
    public List<Notification> selectByExample(@RequestBody NotificationExample example) {
        return notificationService.selectByExample(example);
    }

    // 主键查询通知详情
    @GetMapping("/select/{id}")
    public Notification selectByPrimaryKey(@PathVariable Integer id) {
        return notificationService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody Notification record,
            @RequestBody NotificationExample example) {
        return notificationService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（含BLOB内容）
    @PutMapping("/update/by-example/with-blobs")
    public int updateByExampleWithBLOBs(
            @RequestBody Notification record,
            @RequestBody NotificationExample example) {
        return notificationService.updateByExampleWithBLOBs(record, example);
    }

    // 条件完整更新（基础字段）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody Notification record,
            @RequestBody NotificationExample example) {
        return notificationService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody Notification record) {
        return notificationService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（含BLOB内容）
    @PutMapping("/update/with-blobs")
    public int updateByPrimaryKeyWithBLOBs(@RequestBody Notification record) {
        return notificationService.updateByPrimaryKeyWithBLOBs(record);
    }

    // 主键完整更新（基础字段）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody Notification record) {
        return notificationService.updateByPrimaryKey(record);
    }
}