package com.Axonix.controller;

import com.Axonix.demo.model.LocationShare;
import com.Axonix.demo.model.LocationShareExample;
import com.Axonix.service.LocationShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location-shares")
@CrossOrigin(origins = "http://localhost:5173")
public class LocationShareController {

    @Autowired
    private LocationShareService locationShareService;

    @GetMapping("/count")
    public long count(LocationShareExample example) {
        return locationShareService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return locationShareService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody LocationShare record) {
        return locationShareService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody LocationShare record) {
        return locationShareService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<LocationShare> getList(LocationShareExample example) {
        return locationShareService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public LocationShare getById(@PathVariable Integer id) {
        return locationShareService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody LocationShare record) {
        record.setId(id);
        return locationShareService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody LocationShare record) {
        record.setId(id);
        return locationShareService.updateByIdSelective(record);
    }
}
