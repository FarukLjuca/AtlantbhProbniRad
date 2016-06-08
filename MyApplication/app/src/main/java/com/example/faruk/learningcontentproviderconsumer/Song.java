package com.example.faruk.learningcontentproviderconsumer;

/**
 * Created by Faruk on 07/06/16.
 */
public class Song {
    private String singer;
    private String title;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Song(String singer, String title) {
        this.singer = singer;
        this.title = title;
    }
}
