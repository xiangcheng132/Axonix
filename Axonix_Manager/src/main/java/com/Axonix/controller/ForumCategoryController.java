package com.Axonix.controller;

import com.Axonix.demo.model.ForumCategory;
import com.Axonix.demo.model.ForumCategoryExample;
import com.Axonix.service.ForumCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum-categories")
public class ForumCategoryController {

    @Autowired
    private ForumCategoryService forumCategoryService;

    @GetMapping("/count")
    public long count(ForumCategoryExample example) {
        return forumCategoryService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return forumCategoryService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody ForumCategory record) {
        return forumCategoryService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody ForumCategory record) {
        return forumCategoryService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<ForumCategory> getList(ForumCategoryExample example) {
        return forumCategoryService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public ForumCategory getById(@PathVariable Integer id) {
        return forumCategoryService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody ForumCategory record) {
        record.setId(id);
        return forumCategoryService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody ForumCategory record) {
        record.setId(id);
        return forumCategoryService.updateByIdSelective(record);
    }
}
