package com.Axonix.util;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MyBatisDebugUtil implements CommandLineRunner {

    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisDebugUtil(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void run(String... args) {
        Configuration configuration = sqlSessionFactory.getConfiguration();

        System.out.println("=== MyBatis 加载的 MappedStatements ===");
        Collection<String> statements = configuration.getMappedStatementNames();
        if (statements.isEmpty()) {
            System.out.println("❌ 没有加载任何 SQL 语句！请检查 Mapper.xml 是否正确！");
        } else {
            for (String statement : statements) {
                System.out.println("✅ 已加载: " + statement);
            }
        }
    }
}
