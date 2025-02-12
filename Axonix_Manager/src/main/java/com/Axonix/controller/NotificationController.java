package com.Axonix.controller;

import com.Axonix.demo.model.Notification;
import com.Axonix.demo.model.NotificationExample;
import com.Axonix.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/count")
    public long count(NotificationExample example) {
        return notificationService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return notificationService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody Notification record) {
        return notificationService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody Notification record) {
        return notificationService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<Notification> getList(NotificationExample example) {
        return notificationService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable Integer id) {
        return notificationService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody Notification record) {
        record.setId(id);
        return notificationService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody Notification record) {
        record.setId(id);
        return notificationService.updateByIdSelective(record);
    }
}
