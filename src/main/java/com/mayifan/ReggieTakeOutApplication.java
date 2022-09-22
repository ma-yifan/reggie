package com.mayifan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.mayifan.mapper")
@EnableTransactionManagement
public class ReggieTakeOutApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReggieTakeOutApplication.class, args);
//        System.out.println(context.getBean(DataSourceTransactionManager.class));
        log.info("项目启动成功！");
    }

}
