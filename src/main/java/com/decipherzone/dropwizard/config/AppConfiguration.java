package com.decipherzone.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.MongoClient;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.Valid;


public class AppConfiguration extends Configuration{

    @NotEmpty
    @JsonProperty("appName")
    private String appName;

    @JsonProperty("appBaseUrl")
    private String appBaseUrl;

    @Valid
    @JsonProperty("mongoserver")
    public MongoConfiguration mongoConfiguration = new MongoConfiguration();


    public MongoConfiguration getMongoConfiguration() {
        return mongoConfiguration;
    }

    public void setMongoConfiguration(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    private DbConfig dbConfig = new DbConfig();

    public MongoClient getDataSource() {
        return new MongoClient(dbConfig.getHost(), dbConfig.getPort());
    }

    public String getAppBaseUrl()
    {
        return appBaseUrl;
    }

    public String getAppName() {
        return appName;
    }
}
