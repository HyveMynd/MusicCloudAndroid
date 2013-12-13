package com.hyvemynd.musiccloud.musiclist;

/**
 * Created by andresmonroy on 12/13/13.
 */
public class SongItem {
    final String name;
    final String artist;
    final String album;
    final String url;

    public SongItem(String name, String artist, String album, String url) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getUrl() {
        return url;
    }
}
