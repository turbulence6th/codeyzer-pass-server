package com.codeyzer.pass.sunucu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile({ "dev", "prod" })
public class DataSourceYapilandirma {

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
