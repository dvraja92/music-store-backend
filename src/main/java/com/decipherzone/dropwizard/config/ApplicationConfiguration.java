package com.decipherzone.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

class ApplicationConfiguration extends Configuration {

    @JsonProperty("config")
    private AppConfiguration appConfig;

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    public AppConfiguration getAppConfig() {
        return appConfig;
    }

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }
}
