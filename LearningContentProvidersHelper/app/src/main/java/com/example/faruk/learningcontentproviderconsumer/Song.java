package com.example.faruk.learningcontentproviderconsumer;

/**
 * Created by Faruk on 07/06/16.
 */
public class Song {
    private String singer;
    private String title;

    /**
     * Method for getting singer of song
     * @return Returns singer of the song
     */
    public String getSinger() {
        return singer;
    }

    /**
     * Method for getting title of song
     * @return Returns title of the song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Standard constructor for object Song
     * @param singer Name of singer fo song
     * @param title Title of song
     */
    public Song(String singer, String title) {
        this.singer = singer;
        this.title = title;
    }
}
