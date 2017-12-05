package com.decipherzone.dropwizard.services.impl;

import com.decipherzone.dropwizard.dao.ApplicationDao;
import com.decipherzone.dropwizard.services.ApplicationService;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationDao applicationDao;

    @Inject
    public ApplicationServiceImpl(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    @Override
    public void loadInitialData() {
        applicationDao.loadInitialData();
    }



}
