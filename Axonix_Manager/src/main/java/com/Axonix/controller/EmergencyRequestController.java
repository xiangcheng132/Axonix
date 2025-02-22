package com.Axonix.controller;

import com.Axonix.demo.model.EmergencyRequest;
import com.Axonix.demo.model.EmergencyRequestExample;
import com.Axonix.service.EmergencyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emergency-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class EmergencyRequestController {

    @Autowired
    private EmergencyRequestService emergencyRequestService;

    @GetMapping("/count")
    public long count(EmergencyRequestExample example) {
        return emergencyRequestService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return emergencyRequestService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody EmergencyRequest record) {
        return emergencyRequestService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody EmergencyRequest record) {
        return emergencyRequestService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<EmergencyRequest> getList(EmergencyRequestExample example) {
        return emergencyRequestService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public EmergencyRequest getById(@PathVariable Integer id) {
        return emergencyRequestService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody EmergencyRequest record) {
        record.setId(id);
        return emergencyRequestService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody EmergencyRequest record) {
        record.setId(id);
        return emergencyRequestService.updateByIdSelective(record);
    }
}
