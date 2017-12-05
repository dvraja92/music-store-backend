package com.decipherzone.dropwizard.domain.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel
public class UserLogin
{
    @ApiModelProperty("User email")
    @Email(message = "Please provide a valid email")
    String email;

    @ApiModelProperty("User password")
    @NotEmpty(message = "Please provide password")
    String password;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
