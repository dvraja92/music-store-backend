package com.decipherzone.dropwizard.dao.impl;

import com.decipherzone.dropwizard.AppConstants;
import com.decipherzone.dropwizard.dao.ApplicationDao;
import com.decipherzone.dropwizard.dao.BaseDao;
import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.decipherzone.dropwizard.domain.repositories.MusicRepository;
import com.decipherzone.dropwizard.domain.repositories.impl.MongoBaseRepositoryImpl;
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
public class ApplicationDaoImpl extends MongoBaseRepositoryImpl<Music> implements ApplicationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDaoImpl.class);

    private MusicRepository musicRepository;

    @Inject
    public ApplicationDaoImpl( MongoDb mongoManager, MusicRepository musicRepository) throws Exception {
        super(Music.class, mongoManager);
        this.musicRepository = musicRepository;
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
        List<Music> musicCollection = musicRepository.list();

        if (musicCollection.size() == 0){
            List<Music> musicList = new ArrayList<>();

            try (Stream<String> stream = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(AppConstants.MUSIC_FILE))).lines()) {

                stream.forEach((String res) ->{
                    String[] musicDetailsArr = res.split(",");
                    Music music = new Music();
                    music.prepare();
                    music.setArtist(musicDetailsArr[1].replaceAll("\"", ""));
                    music.setTitle( musicDetailsArr[0].replaceAll("\"", ""));
                    music.setDescription(musicDetailsArr[2].replaceAll("\"", ""));
                    musicList.add(music);
                });

                musicRepository.save(musicList);


                return true;
            } catch (Exception e){
                LOGGER.error(e.getMessage(), e);
                return false;
            }
        }

        return false;
    }

}
