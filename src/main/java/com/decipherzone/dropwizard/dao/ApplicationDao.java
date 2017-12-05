package com.decipherzone.dropwizard.dao;


public interface ApplicationDao {

    /**
     * Load initial data from file to database
     */
    void loadInitialData();

}
