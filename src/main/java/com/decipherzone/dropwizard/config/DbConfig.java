package com.decipherzone.dropwizard.config;

import com.decipherzone.dropwizard.AppConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DbConfig {
    private String username = AppConstants.DB_USERNAME;
    private String password = AppConstants.DB_PSWD;
    private String host = AppConstants.DB_HOST;
    private int port = AppConstants.DB_PORT;
    private String dbName = AppConstants.DB_AUTH;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
