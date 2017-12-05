package com.decipherzone.dropwizard.domain.repositories;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.repositories.base.BaseRepository;

public interface UserRepository extends BaseRepository<UserDetails> {
    UserDetails getUserByEmail(String email);
}
