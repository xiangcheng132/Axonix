package com.Axonix.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理数据库唯一键冲突异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKey(DuplicateKeyException ex) {
        // 根据索引名称判断具体冲突字段
        if (ex.getMessage().contains("idx_unique_username")) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        if (ex.getMessage().contains("idx_unique_phone")) {
            return ResponseEntity.badRequest().body("手机号已注册");
        }
        return ResponseEntity.internalServerError().body("数据库唯一性约束冲突");
    }

    // 通用异常处理（兜底逻辑）
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e) {
//        return "服务器错误：" + e.getMessage();
//    }
}