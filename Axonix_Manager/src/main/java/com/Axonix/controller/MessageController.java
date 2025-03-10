package com.Axonix.controller;

import com.Axonix.demo.model.Message;
import com.Axonix.demo.model.MessageExample;
import com.Axonix.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/count")
    public long count(MessageExample example) {
        return messageService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return messageService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody Message record) {
        return messageService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody Message record) {
        return messageService.insertSelective(record);
    }

    @PostMapping("/list")
    public List<Message> getList(@RequestBody  MessageExample example) {
        return messageService.selectByExampleWithBLOBs(example);
    }

    @GetMapping("/{id}")
    public Message getById(@PathVariable Integer id) {
        return messageService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody Message record) {
        record.setId(id);
        return messageService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody Message record) {
        record.setId(id);
        return messageService.updateByIdSelective(record);
    }
}
