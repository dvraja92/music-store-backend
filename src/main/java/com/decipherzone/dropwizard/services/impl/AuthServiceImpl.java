package com.decipherzone.dropwizard.services.impl;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.models.UserLogin;
import com.decipherzone.dropwizard.domain.models.security.TokenInfo;
import com.decipherzone.dropwizard.domain.repositories.UserRepository;
import com.decipherzone.dropwizard.exceptions.InvalidDataException;
import com.decipherzone.dropwizard.rest.InsertUserDetails;
import com.decipherzone.dropwizard.rest.result.UserResult;
import com.decipherzone.dropwizard.services.AuthService;
import com.decipherzone.dropwizard.util.CredentialsManager;
import com.decipherzone.dropwizard.util.PasswordManager;

import javax.inject.Inject;
import java.util.UUID;

public class AuthServiceImpl implements AuthService{

    private UserRepository userRepository;

    @Inject
    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserResult login(UserLogin login) {
        UserResult userResult = new UserResult();
        UserDetails user  = userRepository.getUserByEmail(login.getEmail());
        if (user == null) {
            throw new InvalidDataException("User","Invalid credentials");
        }
        if (!PasswordManager.INSTANCE.check(login.getPassword(), user.getPassword())) {
            throw new InvalidDataException("User","Invalid credentials");
        }

        String token = CredentialsManager.INSTANCE.getTokenIfUserAlreadyLoggedIn(login.getEmail());
        if (token.isEmpty()) {
            token = UUID.randomUUID().toString().replaceAll("-", "");
            CredentialsManager.INSTANCE.addToken(token, user);
        }
        userResult.setUserDetails(user);
        userResult.setTokenInfo(new TokenInfo(token));

        return userResult;

    }

    @Override
    public UserDetails addUser(InsertUserDetails user) {

        UserDetails userDetails = new UserDetails();
        userDetails.prepare();
        userDetails.setEmail(user.getEmail());
        userDetails.setName(user.getName());
        userDetails.setPassword(PasswordManager.INSTANCE.getSaltedHash(user.getPassword()));

        UserDetails dbUserDetails = userRepository.save(userDetails);
        dbUserDetails.setPassword("");

        return dbUserDetails;
    }
}
