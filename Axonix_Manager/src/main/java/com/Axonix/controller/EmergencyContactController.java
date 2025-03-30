package com.Axonix.controller;

import com.Axonix.demo.model.EmergencyContact;
import com.Axonix.demo.model.EmergencyContactExample;
import com.Axonix.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contact")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    // 统计数量
    @PostMapping("/count")
    public long countByExample(@RequestBody EmergencyContactExample example) {
        return emergencyContactService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody EmergencyContactExample example) {
        return emergencyContactService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return emergencyContactService.deleteByPrimaryKey(id);
    }

    // 完整插入
    @PostMapping("/insert")
    public int insert(@RequestBody EmergencyContact record) {
        return emergencyContactService.insert(record);
    }

    // 选择性插入
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody EmergencyContact record) {
        return emergencyContactService.insertSelective(record);
    }

    // 条件查询
    @PostMapping("/select/by-example")
    public List<EmergencyContact> selectByExample(@RequestBody EmergencyContactExample example) {
        return emergencyContactService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public EmergencyContact selectByPrimaryKey(@PathVariable Integer id) {
        return emergencyContactService.selectByPrimaryKey(id);
    }

    // 条件选择性更新
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody EmergencyContact record,
            @RequestBody EmergencyContactExample example) {
        return emergencyContactService.updateByExampleSelective(record, example);
    }

    // 条件完整更新
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody EmergencyContact record,
            @RequestBody EmergencyContactExample example) {
        return emergencyContactService.updateByExample(record, example);
    }

    // 主键选择性更新
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody EmergencyContact record) {
        return emergencyContactService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody EmergencyContact record) {
        return emergencyContactService.updateByPrimaryKey(record);
    }
}