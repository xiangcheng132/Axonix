package com.Axonix.controller;

import com.Axonix.demo.model.ThirdPartyApiLog;
import com.Axonix.demo.model.ThirdPartyApiLogExample;
import com.Axonix.demo.model.ThirdPartyApiLogWithBLOBs;
import com.Axonix.service.ThirdPartyApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/third-party-api-logs")
@CrossOrigin(origins = "http://localhost:5173")
public class ThirdPartyApiLogController {

    @Autowired
    private ThirdPartyApiLogService thirdPartyApiLogService;

    @GetMapping("/count")
    public long count(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return thirdPartyApiLogService.deleteById(id);
    }

    @PostMapping("/insert")
    public int insert(@RequestBody ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<ThirdPartyApiLog> getList(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public ThirdPartyApiLogWithBLOBs getById(@PathVariable Integer id) {
        return thirdPartyApiLogService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody ThirdPartyApiLogWithBLOBs record) {
        record.setId(id);
        return thirdPartyApiLogService.updateById(record);
    }

    @PutMapping("/{id}/blobs")
    public int updateByIdWithBLOBs(@PathVariable Integer id, @RequestBody ThirdPartyApiLogWithBLOBs record) {
        record.setId(id);
        return thirdPartyApiLogService.updateByIdWithBLOBs(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody ThirdPartyApiLogWithBLOBs record) {
        record.setId(id);
        return thirdPartyApiLogService.updateByIdSelective(record);
    }
}
