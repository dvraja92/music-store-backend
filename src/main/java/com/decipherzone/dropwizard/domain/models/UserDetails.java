package com.decipherzone.dropwizard.domain.models;

import com.decipherzone.dropwizard.domain.annotations.CollectionName;
import com.decipherzone.dropwizard.domain.models.generic.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;

@CollectionName(name = "user_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails extends BaseModel implements Principal{

    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
