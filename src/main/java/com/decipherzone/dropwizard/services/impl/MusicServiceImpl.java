package com.decipherzone.dropwizard.services.impl;

import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.domain.repositories.MusicRepository;
import com.decipherzone.dropwizard.exceptions.InvalidDataException;
import com.decipherzone.dropwizard.rest.MusicAddRequest;
import com.decipherzone.dropwizard.services.MusicService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

public class MusicServiceImpl implements MusicService{
    public static final String MUSIC_RECORD = "Music Record";
    public static final String DATA_NOT_FOUND = "Requested Data Not Found";
    private MusicRepository musicRepository;

    @Inject
    public MusicServiceImpl(MusicRepository musicRepository){
        this.musicRepository = musicRepository;
    }

    @Override
    public Music addMusic(MusicAddRequest request) {
        Music music = new Music();
        music.prepare();
        music.setTitle(request.getTitle());
        music.setArtist(request.getArtist());
        music.setDescription(request.getDescription());
        return musicRepository.save(music);
    }

    @Override
    public List<Music> getAllRecords() {
        return musicRepository.list();
    }

    @Override
    public Music getRecord(String recordId) {
        Music dbMusic = musicRepository.getById(recordId);
        if (dbMusic == null){
            throw new InvalidDataException(MUSIC_RECORD, DATA_NOT_FOUND);
        }

        return musicRepository.getById(recordId);
    }

    @Override
    public Music updateRecord(Music music) {
        Music dbMusic = musicRepository.getById(music.getId());
        if (dbMusic == null){
            throw new InvalidDataException(MUSIC_RECORD, DATA_NOT_FOUND);
        }

        dbMusic.setArtist(music.getArtist());
        dbMusic.setDescription(music.getDescription());
        dbMusic.setTitle(music.getTitle());
        musicRepository.update(dbMusic.getId(), dbMusic);
        return dbMusic;
    }

    @Override
    public void deleteRecord(String recordId) {
        Music dbMusic = musicRepository.getById(recordId);
        if (dbMusic == null){
            throw new InvalidDataException(MUSIC_RECORD, DATA_NOT_FOUND);
        }
        musicRepository.removeById(recordId);
    }

    @Override
    public List<Music> getRecordByArtist(String artistName) {
        if (!StringUtils.isNotBlank(artistName)){
            throw new InvalidDataException(MUSIC_RECORD,"Artist field is empty");
        }

        List<Music> music = musicRepository.getRecordByArtistName(artistName);
        if (music == null){
            throw new InvalidDataException(MUSIC_RECORD,"Record not found");
        }

        return music;
    }

    @Override
    public Music getRecordByTitle(String titleName) {
        if (!StringUtils.isNotBlank(titleName)){
            throw new InvalidDataException(MUSIC_RECORD,"Title field is empty");
        }

        Music music = musicRepository.getRecordByTitle(titleName);
        if (music == null){
            throw new InvalidDataException(MUSIC_RECORD,"Record not found");
        }

        return music;
    }
}
