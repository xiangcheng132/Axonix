package com.Axonix.controller;

import com.Axonix.demo.model.ForumComment;
import com.Axonix.demo.model.ForumCommentExample;
import com.Axonix.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum-comments")
@CrossOrigin(origins = "http://localhost:5173")
public class ForumCommentController {

    @Autowired
    private ForumCommentService forumCommentService;

    @GetMapping("/count")
    public long count(@RequestParam(required = false) Integer postId) {
        ForumCommentExample example = new ForumCommentExample();
        if (postId != null) {
            example.createCriteria().andPostIdEqualTo(postId);
        }
        return forumCommentService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return forumCommentService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody ForumComment record) {
        return forumCommentService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody ForumComment record) {
        return forumCommentService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<ForumComment> getList(@RequestParam(required = false) Integer postId) {
        ForumCommentExample example = new ForumCommentExample();
        if (postId != null) {
            example.createCriteria().andPostIdEqualTo(postId);
        }
        return forumCommentService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public ForumComment getById(@PathVariable Integer id) {
        return forumCommentService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody ForumComment record) {
        record.setId(id);
        return forumCommentService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody ForumComment record) {
        record.setId(id);
        return forumCommentService.updateByIdSelective(record);
    }
}
