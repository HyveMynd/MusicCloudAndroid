package com.hyvemynd.musiccloud.dto;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class SongResponseDto {
    private String name;
    private String artist;
    private String album;
    private int seconds;
    private int sizeMb;

    public SongResponseDto(){
        this("","","",0,0);
    }

    public SongResponseDto(String name, String artist, String album, int seconds, int sizeMb) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.seconds = seconds;
        this.sizeMb = sizeMb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(int sizeMb) {
        this.sizeMb = sizeMb;
    }
}
