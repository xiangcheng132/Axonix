package com.Axonix.controller;

import com.Axonix.demo.model.HelpRequest;
import com.Axonix.demo.model.HelpRequestExample;
import com.Axonix.service.HelpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/help-request")
public class HelpRequestController {

    @Autowired
    private HelpRequestService helpRequestService;

    // 统计求助记录数量
    @PostMapping("/count")
    public long countByExample(@RequestBody HelpRequestExample example) {
        return helpRequestService.countByExample(example);
    }

    // 条件删除求助记录
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody HelpRequestExample example) {
        return helpRequestService.deleteByExample(example);
    }

    // 主键删除求助记录
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return helpRequestService.deleteByPrimaryKey(id);
    }

    // 完整插入求助记录（全字段）
    @PostMapping("/insert")
    public int insert(@RequestBody HelpRequest record) {
        return helpRequestService.insert(record);
    }

    // 选择性插入求助记录（仅非空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody HelpRequest record) {
        return helpRequestService.insertSelective(record);
    }

    // 条件查询求助记录列表
    @PostMapping("/select/by-example")
    public List<HelpRequest> selectByExample(@RequestBody HelpRequestExample example) {
        return helpRequestService.selectByExample(example);
    }

    // 主键查询求助记录详情
    @GetMapping("/select/{id}")
    public HelpRequest selectByPrimaryKey(@PathVariable Integer id) {
        return helpRequestService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（仅更新非空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody HelpRequest record,
            @RequestBody HelpRequestExample example) {
        return helpRequestService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（全字段覆盖）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody HelpRequest record,
            @RequestBody HelpRequestExample example) {
        return helpRequestService.updateByExample(record, example);
    }

    // 主键选择性更新（仅更新非空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody HelpRequest record) {
        return helpRequestService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（全字段覆盖）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody HelpRequest record) {
        return helpRequestService.updateByPrimaryKey(record);
    }
}