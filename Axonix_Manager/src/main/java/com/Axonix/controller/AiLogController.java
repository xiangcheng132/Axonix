package com.Axonix.controller;

import com.Axonix.demo.model.AiLog;
import com.Axonix.demo.model.AiLogExample;
import com.Axonix.service.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/ai-log")
public class AiLogController {

    @Autowired
    private AiLogService aiLogService;

    // 统计数量
    @PostMapping("/count")
    public long countByExample(@RequestBody AiLogExample example) {
        return aiLogService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody AiLogExample example) {
        return aiLogService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return aiLogService.deleteByPrimaryKey(id);
    }

    // 完整插入
    @PostMapping("/insert")
    public int insert(@RequestBody AiLog record) {
        return aiLogService.insert(record);
    }

    // 选择性插入
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody AiLog record) {
        return aiLogService.insertSelective(record);
    }

    // 条件查询
    @PostMapping("/select/by-example")
    public List<AiLog> selectByExample(@RequestBody AiLogExample example) {
        return aiLogService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public AiLog selectByPrimaryKey(@PathVariable Integer id) {
        return aiLogService.selectByPrimaryKey(id);
    }

    // 条件选择性更新
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody AiLog record,
            @RequestBody AiLogExample example) {
        return aiLogService.updateByExampleSelective(record, example);
    }

    // 条件完整更新
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody AiLog record,
            @RequestBody AiLogExample example) {
        return aiLogService.updateByExample(record, example);
    }

    // 主键选择性更新
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody AiLog record) {
        return aiLogService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody AiLog record) {
        return aiLogService.updateByPrimaryKey(record);
    }
}