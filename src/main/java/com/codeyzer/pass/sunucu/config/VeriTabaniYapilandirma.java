package com.codeyzer.pass.sunucu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@EntityScan("com.codeyzer.pass.sunucu.entity")
@EnableJpaRepositories("com.codeyzer.pass.sunucu.repository")
@EnableTransactionManagement
@Configuration
public class VeriTabaniYapilandirma {

    @Value("${DATABASE_URL:postgresql://postgres:postgres@localhost:5432/codeyzer_pass?currentSchema=codeyzer_pass}")
    private String databaseUrl;

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String jdbcDbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        if (dbUri.getQuery() != null) {
            jdbcDbUrl += "?" + dbUri.getQuery();
        }

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(jdbcDbUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}
