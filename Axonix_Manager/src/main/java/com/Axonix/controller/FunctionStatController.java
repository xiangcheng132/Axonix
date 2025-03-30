package com.Axonix.controller;

import com.Axonix.demo.model.FunctionStat;
import com.Axonix.demo.model.FunctionStatExample;
import com.Axonix.service.FunctionStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/function-stat")
public class FunctionStatController {

    @Autowired
    private FunctionStatService functionStatService;

    // 统计记录数
    @PostMapping("/count")
    public long countByExample(@RequestBody FunctionStatExample example) {
        return functionStatService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody FunctionStatExample example) {
        return functionStatService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return functionStatService.deleteByPrimaryKey(id);
    }

    // 完整插入
    @PostMapping("/insert")
    public int insert(@RequestBody FunctionStat record) {
        return functionStatService.insert(record);
    }

    // 选择性插入（忽略空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody FunctionStat record) {
        return functionStatService.insertSelective(record);
    }

    // 条件查询
    @PostMapping("/select/by-example")
    public List<FunctionStat> selectByExample(@RequestBody FunctionStatExample example) {
        return functionStatService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public FunctionStat selectByPrimaryKey(@PathVariable Integer id) {
        return functionStatService.selectByPrimaryKey(id);
    }

    // 条件选择性更新
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody FunctionStat record,
            @RequestBody FunctionStatExample example) {
        return functionStatService.updateByExampleSelective(record, example);
    }

    // 条件完整更新
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody FunctionStat record,
            @RequestBody FunctionStatExample example) {
        return functionStatService.updateByExample(record, example);
    }

    // 主键选择性更新
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody FunctionStat record) {
        return functionStatService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody FunctionStat record) {
        return functionStatService.updateByPrimaryKey(record);
    }
}