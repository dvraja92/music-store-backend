package com.decipherzone.dropwizard.domain.models.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public interface MongoDb {
    /**
     * Get jongo collection
     * @param collectionName
     * @return
     * @throws Exception
     */
    MongoCollection getJongoCollection(String collectionName) throws Exception;

    /**
     * Get Database collection
     * @param collectionName
     * @return
     * @throws Exception
     */
    DBCollection getDBCollection(String collectionName) throws Exception ;

    /**
     * Get jongo object
     * @return
     * @throws Exception
     */
    Jongo getJongo() throws Exception;

    /**
     * Get database object
     * @return
     * @throws Exception
     */
    DB getDB() throws Exception;
}
