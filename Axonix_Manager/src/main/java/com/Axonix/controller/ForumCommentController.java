package com.Axonix.controller;

import com.Axonix.demo.model.ForumComment;
import com.Axonix.demo.model.ForumCommentExample;
import com.Axonix.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum-comment")
public class ForumCommentController {

    @Autowired
    private ForumCommentService forumCommentService;

    // 统计数量
    @PostMapping("/count")
    public long countByExample(@RequestBody ForumCommentExample example) {
        return forumCommentService.countByExample(example);
    }

    // 条件删除
    @DeleteMapping("/delete/by-example")
    public int deleteByExample(@RequestBody ForumCommentExample example) {
        return forumCommentService.deleteByExample(example);
    }

    // 主键删除
    @DeleteMapping("/delete/{id}")
    public int deleteByPrimaryKey(@PathVariable Integer id) {
        return forumCommentService.deleteByPrimaryKey(id);
    }

    // 完整插入（包含BLOB字段）
    @PostMapping("/insert")
    public int insert(@RequestBody ForumComment record) {
        return forumCommentService.insert(record);
    }

    // 选择性插入（忽略空字段）
    @PostMapping("/insert/selective")
    public int insertSelective(@RequestBody ForumComment record) {
        return forumCommentService.insertSelective(record);
    }

    // 条件查询（包含BLOB字段）
    @PostMapping("/select/by-example-with-blobs")
    public List<ForumComment> selectByExampleWithBLOBs(@RequestBody ForumCommentExample example) {
        return forumCommentService.selectByExampleWithBLOBs(example);
    }

    // 条件查询（不包含BLOB字段）
    @PostMapping("/select/by-example")
    public List<ForumComment> selectByExample(@RequestBody ForumCommentExample example) {
        return forumCommentService.selectByExample(example);
    }

    // 主键查询
    @GetMapping("/select/{id}")
    public ForumComment selectByPrimaryKey(@PathVariable Integer id) {
        return forumCommentService.selectByPrimaryKey(id);
    }

    // 条件选择性更新（忽略空字段）
    @PutMapping("/update/by-example/selective")
    public int updateByExampleSelective(
            @RequestBody ForumComment record,
            @RequestBody ForumCommentExample example) {
        return forumCommentService.updateByExampleSelective(record, example);
    }

    // 条件完整更新（包含BLOB字段）
    @PutMapping("/update/by-example/with-blobs")
    public int updateByExampleWithBLOBs(
            @RequestBody ForumComment record,
            @RequestBody ForumCommentExample example) {
        return forumCommentService.updateByExampleWithBLOBs(record, example);
    }

    // 条件完整更新（不包含BLOB字段）
    @PutMapping("/update/by-example")
    public int updateByExample(
            @RequestBody ForumComment record,
            @RequestBody ForumCommentExample example) {
        return forumCommentService.updateByExample(record, example);
    }

    // 主键选择性更新（忽略空字段）
    @PutMapping("/update/selective")
    public int updateByPrimaryKeySelective(@RequestBody ForumComment record) {
        return forumCommentService.updateByPrimaryKeySelective(record);
    }

    // 主键完整更新（包含BLOB字段）
    @PutMapping("/update/with-blobs")
    public int updateByPrimaryKeyWithBLOBs(@RequestBody ForumComment record) {
        return forumCommentService.updateByPrimaryKeyWithBLOBs(record);
    }

    // 主键完整更新（不包含BLOB字段）
    @PutMapping("/update")
    public int updateByPrimaryKey(@RequestBody ForumComment record) {
        return forumCommentService.updateByPrimaryKey(record);
    }
}