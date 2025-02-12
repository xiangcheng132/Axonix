package com.Axonix.controller;

import com.Axonix.demo.model.Facility;
import com.Axonix.demo.model.FacilityExample;
import com.Axonix.demo.model.FacilityWithBLOBs;
import com.Axonix.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/count")
    public long count(FacilityExample example) {
        return facilityService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return facilityService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody FacilityWithBLOBs record) {
        return facilityService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody FacilityWithBLOBs record) {
        return facilityService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<Facility> getList(FacilityExample example) {
        return facilityService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public FacilityWithBLOBs getById(@PathVariable Integer id) {
        return facilityService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody Facility record) {
        record.setId(id);
        return facilityService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody FacilityWithBLOBs record) {
        record.setId(id);
        return facilityService.updateByIdSelective(record);
    }
}
