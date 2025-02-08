package com.Axonix.demo.mapper;

import com.Axonix.demo.model.SignLanguageTranslationLog;
import com.Axonix.demo.model.SignLanguageTranslationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SignLanguageTranslationLogMapper {
    long countByExample(SignLanguageTranslationLogExample example);

    int deleteByExample(SignLanguageTranslationLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SignLanguageTranslationLog record);

    int insertSelective(SignLanguageTranslationLog record);

    List<SignLanguageTranslationLog> selectByExampleWithBLOBs(SignLanguageTranslationLogExample example);

    List<SignLanguageTranslationLog> selectByExample(SignLanguageTranslationLogExample example);

    SignLanguageTranslationLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SignLanguageTranslationLog record, @Param("example") SignLanguageTranslationLogExample example);

    int updateByExampleWithBLOBs(@Param("record") SignLanguageTranslationLog record, @Param("example") SignLanguageTranslationLogExample example);

    int updateByExample(@Param("record") SignLanguageTranslationLog record, @Param("example") SignLanguageTranslationLogExample example);

    int updateByPrimaryKeySelective(SignLanguageTranslationLog record);

    int updateByPrimaryKeyWithBLOBs(SignLanguageTranslationLog record);

    int updateByPrimaryKey(SignLanguageTranslationLog record);
}