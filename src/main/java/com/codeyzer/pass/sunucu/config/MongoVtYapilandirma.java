package com.codeyzer.pass.sunucu.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoVtYapilandirma {

    private String s = "mongodb+srv://otanrikulu:eLTNx3tn0B2e7OWF@codeyzerpass.094zk.mongodb.net/CodeyzerPass?retryWrites=true&w=majority";

    @Value("${MONGO_URL:mongodb://mongo:mongo@localhost}")
    private String mongoUrl;

    @Bean
    public MongoClient mongoDatabase() {
        ConnectionString connectionString = new ConnectionString(mongoUrl);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        return MongoClients.create(settings);
    }
}
