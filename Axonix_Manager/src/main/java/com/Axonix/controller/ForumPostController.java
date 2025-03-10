package com.Axonix.controller;

import com.Axonix.demo.model.ForumPost;
import com.Axonix.demo.model.ForumPostExample;
import com.Axonix.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum-posts")
@CrossOrigin(origins = "http://localhost:5173")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @GetMapping("/count")
    public long count(ForumPostExample example) {
        return forumPostService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return forumPostService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody ForumPost record) {
        return forumPostService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody ForumPost record) {
        return forumPostService.insertSelective(record);
    }

    @PostMapping("/list")
    public List<ForumPost> getList(@RequestBody ForumPostExample example) {
        return forumPostService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public ForumPost getById(@PathVariable Integer id) {
        return forumPostService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody ForumPost record) {
        record.setId(id);
        return forumPostService.updateByIdWithBLOBs(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody ForumPost record) {
        record.setId(id);
        return forumPostService.updateByIdSelective(record);
    }
}
