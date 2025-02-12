package com.Axonix.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

    // 配置数据源
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");//驱动类，一般分为"com.mysql.jdbc.Driver"和"com.mysql.cj.jdbc.Driver",后者为新版的
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/axonix?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");//数据库地址
        dataSource.setUsername("root");//数据库用户名
        dataSource.setPassword("123456");//数据库密码
        return dataSource;
    }

    // 配置 SqlSessionFactory
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }



    // 配置事务管理器
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
