package com.decipherzone.dropwizard.domain.repositories.impl;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.decipherzone.dropwizard.domain.repositories.UserRepository;

import javax.inject.Inject;

public class UserRepositoryImpl extends MongoBaseRepositoryImpl<UserDetails> implements UserRepository{

    @Inject
    public UserRepositoryImpl(MongoDb mongoManager) throws Exception {
        super(UserDetails.class, mongoManager);
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return coll.findOne("{email:#, deleted:false}", email).as(entityClazz);
    }
}
