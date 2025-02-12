package com.Axonix.controller;

import com.Axonix.demo.model.EmergencyContact;
import com.Axonix.demo.model.EmergencyContactExample;
import com.Axonix.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emergency-contacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @GetMapping("/count")
    public long count(EmergencyContactExample example) {
        return emergencyContactService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return emergencyContactService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody EmergencyContact record) {
        return emergencyContactService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody EmergencyContact record) {
        return emergencyContactService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<EmergencyContact> getList(EmergencyContactExample example) {
        return emergencyContactService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public EmergencyContact getById(@PathVariable Integer id) {
        return emergencyContactService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody EmergencyContact record) {
        record.setId(id);
        return emergencyContactService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody EmergencyContact record) {
        record.setId(id);
        return emergencyContactService.updateByIdSelective(record);
    }
}
