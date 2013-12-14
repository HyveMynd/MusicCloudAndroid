package com.hyvemynd.musiccloud.dto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class SongRequestDto {
    public String Name;
    public String Artist;
    public String Album;
    public int Seconds;
    public int SizeMb;
    public String Email;

    public SongRequestDto() {
        this("","","",0,0,"");
    }

    public SongRequestDto(String name, String artist, String album, int seconds, int sizeMb, String email) {
        this.Name = name;
        this.Artist = artist;
        this.Album = album;
        this.Seconds = seconds;
        this.SizeMb = sizeMb;
        this.Email = email;
    }
}
