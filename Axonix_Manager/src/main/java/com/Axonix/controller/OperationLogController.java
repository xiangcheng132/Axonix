package com.Axonix.controller;

import com.Axonix.demo.model.OperationLog;
import com.Axonix.demo.model.OperationLogExample;
import com.Axonix.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operationLogs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("/count")
    public long count(OperationLogExample example) {
        return operationLogService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return operationLogService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody OperationLog record) {
        return operationLogService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody OperationLog record) {
        return operationLogService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<OperationLog> getList(OperationLogExample example) {
        return operationLogService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public OperationLog getById(@PathVariable Integer id) {
        return operationLogService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody OperationLog record) {
        record.setId(id);
        return operationLogService.updateById(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody OperationLog record) {
        record.setId(id);
        return operationLogService.updateByIdSelective(record);
    }
}
