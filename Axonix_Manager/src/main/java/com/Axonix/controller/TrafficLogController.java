package com.Axonix.controller;

import com.Axonix.demo.model.TrafficLog;
import com.Axonix.demo.model.TrafficLogExample;
import com.Axonix.service.TrafficLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/traffic-logs")
public class TrafficLogController {

    @Autowired
    private TrafficLogService trafficLogService;

    // 统计路况记录数量
    @PostMapping("/count")
    public long countByExample(@RequestBody TrafficLogExample example) {
        return trafficLogService.countByExample(example);
    }

    // 条件删除路况记录
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody TrafficLogExample example) {
        return trafficLogService.deleteByExample(example);
    }

    // 主键删除路况记录
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return trafficLogService.deleteByPrimaryKey(id);
    }

    // 完整插入路况记录
    @PostMapping("/insert")
    public int insert(@RequestBody TrafficLog record) {
        return trafficLogService.insert(record);
    }

    // 选择性插入路况记录（仅非空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody TrafficLog record) {
        return trafficLogService.insertSelective(record);
    }

    // 条件查询路况记录列表
    @PostMapping("/select/by-example")
    public List<TrafficLog> selectByExample(@RequestBody TrafficLogExample example) {
        return trafficLogService.selectByExample(example);
    }

    // 主键查询路况记录详情
    @GetMapping("/select/{id}")
    public TrafficLog selectByPrimaryKey(@PathVariable Integer id) {
        return trafficLogService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody TrafficLog record,
            @RequestBody TrafficLogExample example) {
        return trafficLogService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（全字段覆盖）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody TrafficLog record,
            @RequestBody TrafficLogExample example) {
        return trafficLogService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody TrafficLog record) {
        return trafficLogService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（全字段覆盖）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody TrafficLog record) {
        return trafficLogService.updateByPrimaryKey(record);
    }
}