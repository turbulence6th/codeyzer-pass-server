package com.codeyzer.pass.sunucu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EntityScan("com.codeyzer.pass.sunucu.entity")
@EnableJpaRepositories("com.codeyzer.pass.sunucu.repository")
@EnableTransactionManagement
@Configuration
public class VeriTabaniYapilandirma {

    @Value("${DATABASE_URL:postgresql://postgres:postgres@localhost:5432/codeyzer_pass?currentSchema=codeyzer_pass}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        Matcher matcher = Pattern.compile("(?<protocol>.*?)://(?<username>.*?):(?<password>.*?)@(?<ip>.*?):(?<port>.*?)(?<path>/.*)").matcher(databaseUrl);
        if (matcher.find()) {
            String protocol = matcher.group("protocol");
            String username = matcher.group("username");
            String password = matcher.group("password");
            String ip = matcher.group("ip");
            String port = matcher.group("port");
            String path = matcher.group("path");

            String jdbcDbUrl = "jdbc:" + protocol + "://" + ip + ':' + port + path;

            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url(jdbcDbUrl);
            dataSourceBuilder.username(username);
            dataSourceBuilder.password(password);
            return dataSourceBuilder.build();

        } else {
            throw new RuntimeException("Database url hatalıdır");
        }
    }
}
