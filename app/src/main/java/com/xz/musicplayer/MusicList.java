package com.xz.musicplayer;

import java.io.Serializable;

public class MusicList {

    int id;
    String title;
    String singer;
    String album;
    String length;
    String year;
    String image;
    int duration;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public String getImage() {
        return image;
    }

    public String getAlbum() { return album; }

    public String getLength() {
        return length;
    }

    public String getYear() {
        return year;
    }

    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return super.toString();
    }
}
