package com.decipherzone.dropwizard.rest.result;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.models.security.TokenInfo;
import java.io.Serializable;


public class UserResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private TokenInfo tokenInfo;
    private UserDetails userDetails;



    public TokenInfo getTokenInfo()
    {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo)
    {
        this.tokenInfo = tokenInfo;
    }

    public UserDetails getUserDetails()
    {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails)
    {
        this.userDetails = userDetails;
    }

}
