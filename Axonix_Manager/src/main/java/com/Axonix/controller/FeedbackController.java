package com.Axonix.controller;

import com.Axonix.demo.model.Feedback;
import com.Axonix.demo.model.FeedbackExample;
import com.Axonix.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // 统计数量
    @PostMapping("/count")
    public long countByExample(@RequestBody FeedbackExample example) {
        return feedbackService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody FeedbackExample example) {
        return feedbackService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return feedbackService.deleteByPrimaryKey(id);
    }

    // 完整插入（包含BLOB字段）
    @PostMapping("/insert")
    public int insert(@RequestBody Feedback record) {
        return feedbackService.insert(record);
    }

    // 选择性插入（忽略空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody Feedback record) {
        return feedbackService.insertSelective(record);
    }

    // 条件查询（包含BLOB字段）
    @PostMapping("/select/by-example-with-blobs")
    public List<Feedback> selectByExampleWithBLOBs(@RequestBody FeedbackExample example) {
        return feedbackService.selectByExampleWithBLOBs(example);
    }

    // 条件查询（不包含BLOB字段）
    @PostMapping("/select/by-example")
    public List<Feedback> selectByExample(@RequestBody FeedbackExample example) {
        return feedbackService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public Feedback selectByPrimaryKey(@PathVariable Integer id) {
        return feedbackService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody Feedback record,
            @RequestBody FeedbackExample example) {
        return feedbackService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（包含BLOB字段）
    @PutMapping("/update/by-example/with-blobs")
    public int updateByExampleWithBLOBs(
            @RequestBody Feedback record,
            @RequestBody FeedbackExample example) {
        return feedbackService.updateByExampleWithBLOBs(record, example);
    }

    // 条件完整更新（不包含BLOB字段）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody Feedback record,
            @RequestBody FeedbackExample example) {
        return feedbackService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody Feedback record) {
        return feedbackService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（包含BLOB字段）
    @PutMapping("/update/with-blobs")
    public int updateByPrimaryKeyWithBLOBs(@RequestBody Feedback record) {
        return feedbackService.updateByPrimaryKeyWithBLOBs(record);
    }

    // 主键完整更新（不包含BLOB字段）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody Feedback record) {
        return feedbackService.updateByPrimaryKey(record);
    }
}