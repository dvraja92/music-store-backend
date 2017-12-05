package com.decipherzone.dropwizard.dao;

import com.decipherzone.dropwizard.AppConstants;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;


public interface BaseDao<T> {

    /**
     * Get collection from mongo database
     * @param mongoClient
     * @param collectionName
     * @param returnClass
     * @return
     */
    default MongoCollection<T> getCollection(MongoClient mongoClient, String collectionName, Class<T> returnClass){
        return mongoClient.getDatabase(AppConstants.APP_DATABASE_NAME).getCollection(collectionName, returnClass);
    }

}
