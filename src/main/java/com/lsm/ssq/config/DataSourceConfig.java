package com.lsm.ssq.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.driverClassName}")
    private String driverClassName;

    @Value("${datasource.jpa.url}")
    private String url;

    @Bean
    public DataSource jpaDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(15);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(280);
        dataSource.setMaxWait(30000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }

}
