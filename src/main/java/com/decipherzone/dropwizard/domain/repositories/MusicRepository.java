package com.decipherzone.dropwizard.domain.repositories;

import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.domain.repositories.base.BaseRepository;

import java.util.List;

public interface MusicRepository extends BaseRepository<Music>{
    List<Music> getRecordByArtistName(String artistName);

    Music getRecordByTitle(String titleName);
}
