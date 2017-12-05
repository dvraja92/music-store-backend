package com.decipherzone.dropwizard.domain.models;

import com.decipherzone.dropwizard.domain.annotations.CollectionName;
import com.decipherzone.dropwizard.domain.models.generic.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@CollectionName(name = "musics", indexes = {"{title:1}"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Music extends BaseModel{
    private String artist;
    private String title;
    private String description;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
