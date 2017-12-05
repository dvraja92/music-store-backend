package com.decipherzone.dropwizard.dao;


import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.domain.repositories.base.BaseRepository;

public interface ApplicationDao extends BaseRepository<Music>{

    /**
     * Load initial data from file to database
     */
    void loadInitialData();

}
