package com.decipherzone.dropwizard.domain.repositories.impl;

import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.decipherzone.dropwizard.domain.repositories.MusicRepository;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.List;

public class MusicRepositoryImpl extends MongoBaseRepositoryImpl<Music> implements MusicRepository{

    @Inject
    public MusicRepositoryImpl(MongoDb mongoManager) throws Exception {
        super(Music.class, mongoManager);
    }

    @Override
    public List<Music> getRecordByArtistName(String artistName) {
        Iterable<Music> iterable = coll.find("{artist:#, deleted:false}", artistName).as(entityClazz);
        return Lists.newArrayList(iterable);
    }

    @Override
    public Music getRecordByTitle(String titleName) {
        return coll.findOne("{title:#, deleted:false}", titleName).as(entityClazz);
    }
}
