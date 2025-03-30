package com.Axonix.controller;

import com.Axonix.demo.model.Vip;
import com.Axonix.demo.model.VipExample;
import com.Axonix.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/vips")
public class VipController {

    @Autowired
    private VipService vipService;

    // 统计VIP记录数
    @PostMapping("/count")
    public long countByExample(@RequestBody VipExample example) {
        return vipService.countByExample(example);
    }

    // 条件删除VIP记录
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody VipExample example) {
        return vipService.deleteByExample(example);
    }

    // 主键删除VIP记录
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return vipService.deleteByPrimaryKey(id);
    }

    // 完整新增VIP记录
    @PostMapping("/insert")
    public int insert(@RequestBody Vip record) {
        return vipService.insert(record);
    }

    // 选择性新增VIP记录（推荐）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody Vip record) {
        return vipService.insertSelective(record);
    }

    // 条件查询VIP记录列表
    @PostMapping("/select/by-example")
    public List<Vip> selectByExample(@RequestBody VipExample example) {
        return vipService.selectByExample(example);
    }

    // 主键查询VIP记录详情
    @GetMapping("/select/{id}")
    public Vip selectByPrimaryKey(@PathVariable Integer id) {
        return vipService.selectByPrimaryKey(id);
    }

    // 条件选择性更新VIP记录
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody Vip record,
            @RequestBody VipExample example) {
        return vipService.updateByExampleSelective(record, example);
    }

    // 条件完整更新VIP记录
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody Vip record,
            @RequestBody VipExample example) {
        return vipService.updateByExample(record, example);
    }

    // 主键选择性更新VIP记录（推荐）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody Vip record) {
        return vipService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新VIP记录
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody Vip record) {
        return vipService.updateByPrimaryKey(record);
    }
}