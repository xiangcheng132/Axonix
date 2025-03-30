package com.Axonix.controller;

import com.Axonix.demo.model.ForumPost;
import com.Axonix.demo.model.ForumPostExample;
import com.Axonix.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/forum-post")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    // 统计帖子数量
    @PostMapping("/count")
    public long countByExample(@RequestBody ForumPostExample example) {
        return forumPostService.countByExample(example);
    }

    // 条件删除帖子
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody ForumPostExample example) {
        return forumPostService.deleteByExample(example);
    }

    // 主键删除帖子
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return forumPostService.deleteByPrimaryKey(id);
    }

    // 完整插入帖子（含BLOB内容）
    @PostMapping("/insert")
    public int insert(@RequestBody ForumPost record) {
        return forumPostService.insert(record);
    }

    // 选择性插入帖子（仅非空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody ForumPost record) {
        return forumPostService.insertSelective(record);
    }

    // 条件查询帖子（带BLOB内容）
    @PostMapping("/select/by-example-with-blobs")
    public List<ForumPost> selectByExampleWithBLOBs(@RequestBody ForumPostExample example) {
        return forumPostService.selectByExampleWithBLOBs(example);
    }

    // 条件查询帖子（基础字段）
    @PostMapping("/select/by-example")
    public List<ForumPost> selectByExample(@RequestBody ForumPostExample example) {
        return forumPostService.selectByExample(example);
    }

    // 主键查询帖子
    @GetMapping("/select/{id}")
    public ForumPost selectByPrimaryKey(@PathVariable Integer id) {
        return forumPostService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody ForumPost record,
            @RequestBody ForumPostExample example) {
        return forumPostService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（含BLOB内容）
    @PutMapping("/update/by-example/with-blobs")
    public int updateByExampleWithBLOBs(
            @RequestBody ForumPost record,
            @RequestBody ForumPostExample example) {
        return forumPostService.updateByExampleWithBLOBs(record, example);
    }

    // 条件完整更新（基础字段）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody ForumPost record,
            @RequestBody ForumPostExample example) {
        return forumPostService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody ForumPost record) {
        return forumPostService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（含BLOB内容）
    @PutMapping("/update/with-blobs")
    public int updateByPrimaryKeyWithBLOBs(@RequestBody ForumPost record) {
        return forumPostService.updateByPrimaryKeyWithBLOBs(record);
    }

    // 主键完整更新（基础字段）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody ForumPost record) {
        return forumPostService.updateByPrimaryKey(record);
    }
}