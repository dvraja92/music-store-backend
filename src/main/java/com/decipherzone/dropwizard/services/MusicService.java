package com.decipherzone.dropwizard.services;

import com.decipherzone.dropwizard.domain.models.Music;
import com.decipherzone.dropwizard.rest.MusicAddRequest;

import java.util.List;

public interface MusicService {
    /**
     * Add Music into record
     * @param request
     * @return Music
     */
    Music addMusic(MusicAddRequest request);

    /**
     * Getting list of record
     * @return List of record
     */
    List<Music> getAllRecords();

    /**
     * Get music by Id
     * @param recordId
     * @return Music
     */
    Music getRecord(String recordId);

    /**
     * Update music record
     * @param music
     * @return Music
     */
    Music updateRecord(Music music);

    /**
     * Delete music record by Id
     * @param recordId
     */
    void deleteRecord(String recordId);

    /**
     * Getting list of music by artist
     * @param artistName
     * @return Lis of music
     */
    List<Music> getRecordByArtist(String artistName);

    /**
     * Get music record by title name
     * @param titleName
     * @return Music
     */
    Music getRecordByTitle(String titleName);
}
