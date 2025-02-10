package com.Axonix.service;

import com.Axonix.demo.model.SignLanguageTranslationLog;
import com.Axonix.demo.model.SignLanguageTranslationLogExample;
import java.util.List;

public interface SignLanguageTranslationLogService {

    long countByExample(SignLanguageTranslationLogExample example);

    int deleteByExample(SignLanguageTranslationLogExample example);

    int deleteById(Integer id);

    int insert(SignLanguageTranslationLog record);

    int insertSelective(SignLanguageTranslationLog record);

    List<SignLanguageTranslationLog> selectByExampleWithBLOBs(SignLanguageTranslationLogExample example);

    List<SignLanguageTranslationLog> selectByExample(SignLanguageTranslationLogExample example);

    SignLanguageTranslationLog selectById(Integer id);

    int updateByExampleSelective(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example);

    int updateByExampleWithBLOBs(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example);

    int updateByExample(SignLanguageTranslationLog record, SignLanguageTranslationLogExample example);

    int updateByIdSelective(SignLanguageTranslationLog record);

    int updateByIdWithBLOBs(SignLanguageTranslationLog record);

    int updateById(SignLanguageTranslationLog record);
}
