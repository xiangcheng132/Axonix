package com.Axonix.controller;

import com.Axonix.demo.model.SignLanguageTranslationLog;
import com.Axonix.demo.model.SignLanguageTranslationLogExample;
import com.Axonix.service.SignLanguageTranslationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sign-language-translation-logs")
public class SignLanguageTranslationLogController {

    @Autowired
    private SignLanguageTranslationLogService signLanguageTranslationLogService;

    @GetMapping("/count")
    public long count(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogService.countByExample(example);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Integer id) {
        return signLanguageTranslationLogService.deleteById(id);
    }

    @PostMapping("/")
    public int insert(@RequestBody SignLanguageTranslationLog record) {
        return signLanguageTranslationLogService.insert(record);
    }

    @PostMapping("/selective")
    public int insertSelective(@RequestBody SignLanguageTranslationLog record) {
        return signLanguageTranslationLogService.insertSelective(record);
    }

    @GetMapping("/list")
    public List<SignLanguageTranslationLog> getList(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogService.selectByExample(example);
    }

    @GetMapping("/{id}")
    public SignLanguageTranslationLog getById(@PathVariable Integer id) {
        return signLanguageTranslationLogService.selectById(id);
    }

    @PutMapping("/{id}")
    public int updateById(@PathVariable Integer id, @RequestBody SignLanguageTranslationLog record) {
        record.setId(id);
        return signLanguageTranslationLogService.updateById(record);
    }

    @PutMapping("/{id}/blobs")
    public int updateByIdWithBLOBs(@PathVariable Integer id, @RequestBody SignLanguageTranslationLog record) {
        record.setId(id);
        return signLanguageTranslationLogService.updateByIdWithBLOBs(record);
    }

    @PatchMapping("/{id}/selective")
    public int updateByIdSelective(@PathVariable Integer id, @RequestBody SignLanguageTranslationLog record) {
        record.setId(id);
        return signLanguageTranslationLogService.updateByIdSelective(record);
    }
}
