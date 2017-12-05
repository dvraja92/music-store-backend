package com.decipherzone.dropwizard.config;

import com.decipherzone.dropwizard.AppConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class MongoConfiguration {
    @NotEmpty
    @JsonProperty
    private String uri;

    public String getUri() {
        return AppConstants.URI;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


}
