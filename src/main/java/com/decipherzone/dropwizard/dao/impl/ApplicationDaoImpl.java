package com.decipherzone.dropwizard.dao.impl;

import com.decipherzone.dropwizard.AppConstants;
import com.decipherzone.dropwizard.dao.ApplicationDao;
import com.decipherzone.dropwizard.dao.BaseDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class ApplicationDaoImpl implements ApplicationDao, BaseDao<BasicDBObject> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDaoImpl.class);

    private MongoClient mongoClient;

    @Inject
    public ApplicationDaoImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void loadInitialData() {
        loadMusicData();
    }

    /**
     * Load music data from file to data base
     * @return
     */
    private boolean loadMusicData(){
        MongoCollection<BasicDBObject> musicCollection = getCollection(mongoClient, AppConstants.MUSIC_COLLECTION, BasicDBObject.class);

        if (musicCollection.count() == 0){
            List<BasicDBObject> musicList = new ArrayList<>();

            try (Stream<String> stream = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(AppConstants.MUSIC_FILE))).lines()) {

                stream.forEach((String res) ->{
                    String[] musicDetailsArr = res.split(",");
                    BasicDBObject music = new BasicDBObject();
                    music.put("title", musicDetailsArr[0].replaceAll("\"", ""));
                    music.put("artist", musicDetailsArr[1].replaceAll("\"", ""));
                    music.put("description", musicDetailsArr[2].replaceAll("\"", ""));

                    musicList.add(music);
                });

                musicCollection.insertMany(musicList);

                return true;
            } catch (Exception e){
                LOGGER.error(e.getMessage(), e);
                return false;
            }
        }

        return false;
    }

}
