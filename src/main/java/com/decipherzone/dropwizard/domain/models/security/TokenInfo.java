package com.decipherzone.dropwizard.domain.models.security;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class TokenInfo implements Serializable
{
    @NotEmpty
    private String accessToken;

    public TokenInfo() {
    }

    public TokenInfo(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

}
