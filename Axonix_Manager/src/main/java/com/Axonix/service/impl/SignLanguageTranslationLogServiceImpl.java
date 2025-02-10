package com.Axonix.service.impl;

import com.Axonix.demo.mapper.SignLanguageTranslationLogMapper;
import com.Axonix.demo.model.SignLanguageTranslationLog;
import com.Axonix.demo.model.SignLanguageTranslationLogExample;
import com.Axonix.service.SignLanguageTranslationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignLanguageTranslationLogServiceImpl implements SignLanguageTranslationLogService {

    @Autowired
    private SignLanguageTranslationLogMapper signLanguageTranslationLogMapper;

    @Override
    public long countByExample(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return signLanguageTranslationLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SignLanguageTranslationLog record) {
        return signLanguageTranslationLogMapper.insert(record);
    }

    @Override
    public int insertSelective(SignLanguageTranslationLog record) {
        return signLanguageTranslationLogMapper.insertSelective(record);
    }

    @Override
    public List<SignLanguageTranslationLog> selectByExampleWithBLOBs(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<SignLanguageTranslationLog> selectByExample(SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.selectByExample(example);
    }

    @Override
    public SignLanguageTranslationLog selectById(Integer id) {
        return signLanguageTranslationLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example) {
        return signLanguageTranslationLogMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(SignLanguageTranslationLog record) {
        return signLanguageTranslationLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(SignLanguageTranslationLog record) {
        return signLanguageTranslationLogMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(SignLanguageTranslationLog record) {
        return signLanguageTranslationLogMapper.updateByPrimaryKey(record);
    }
}
