package com.decipherzone.dropwizard.domain.mongo;

import com.decipherzone.dropwizard.config.AppConfiguration;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.*;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

@Singleton
public class MongoManager implements Managed, MongoDb {
    @Inject
    private AppConfiguration config;
    private  MongoClientURI clientURI;
    private  Jongo jongo;

    private  MongoClient client;
    static final Log LOG = LogFactory.getLog(MongoManager.class);

    public MongoManager() {
        LOG.info("Initiating MongoManager");
    }

    public MongoManager(AppConfiguration config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        // Only start if client isn't started
        if (client != null) return;
        LOG.info("Starting MongoManager");

        String mongoUri = config.getMongoConfiguration().getUri();
        clientURI = new MongoClientURI(mongoUri);
        client = new MongoClient(clientURI);

        DB db = client.getDB(clientURI.getDatabase());
        db.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        jongo = new Jongo(db,
                new JacksonMapper.Builder()
                        .registerModule(new JodaModule())
                        .enable(MapperFeature.AUTO_DETECT_GETTERS)
                        .build());
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Stopping MongoManager - DB");

        if (client != null) {
            client.close();
            client = null;
        }
    }

    @Override
    public MongoCollection getJongoCollection(String collectionName) throws Exception {
        start();
        return jongo.getCollection(collectionName);
    }

    @Override
    public DBCollection getDBCollection(String collectionName) throws Exception {
        start();

        DB db = client.getDB(clientURI.getDatabase());

        return db.getCollection(collectionName);
    }

    @Override
    public Jongo getJongo() throws Exception {
        start();
        return jongo;
    }

    @Override
    public DB getDB() throws Exception {
        start();
        return client.getDB(clientURI.getDatabase());
    }

}
