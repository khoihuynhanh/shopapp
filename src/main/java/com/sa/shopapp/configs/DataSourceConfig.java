package com.sa.shopapp.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("oracle.jdbc.driver.OracleDriver");
        dataSourceBuilder.url("jdbc:oracle:thin:@//localhost:1522/shopapp");
        dataSourceBuilder.username("C##AAKHOII");
        dataSourceBuilder.password("Abc123456789@");
        return dataSourceBuilder.build();
    }
}
