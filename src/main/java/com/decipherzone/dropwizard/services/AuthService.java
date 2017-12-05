package com.decipherzone.dropwizard.services;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.models.UserLogin;
import com.decipherzone.dropwizard.rest.InsertUserDetails;
import com.decipherzone.dropwizard.rest.result.UserResult;

public interface AuthService {
    /**
     * This method is used login for app
     * @param login
     * @return User with token information on success
     */
    UserResult login(UserLogin login);

    /**
     * This method add user for app
     * @param user
     * @return User
     */
    UserDetails addUser(InsertUserDetails user);
}
