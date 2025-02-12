package com.Axonix.controller;

import com.Axonix.demo.model.FriendRelationship;
import com.Axonix.demo.model.FriendRelationshipExample;
import com.Axonix.service.FriendRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend-relationships")
public class FriendRelationshipController {

    @Autowired
    private FriendRelationshipService friendRelationshipService;

    @GetMapping("/count")
    public long count(FriendRelationshipExample example) {
        return friendRelationshipService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return friendRelationshipService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody FriendRelationship record) {
        return friendRelationshipService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody FriendRelationship record) {
        return friendRelationshipService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<FriendRelationship> getList(FriendRelationshipExample example) {
        return friendRelationshipService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public FriendRelationship getById(@PathVariable Integer id) {
        return friendRelationshipService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody FriendRelationship record) {
        record.setId(id);
        return friendRelationshipService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody FriendRelationship record) {
        record.setId(id);
        return friendRelationshipService.updateByIdSelective(record);
    }
}
