package com.hyvemynd.musiccloud;

import java.util.ArrayList;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicCloudModel {
    private static MusicCloudModel model;

    private ArrayList<String> songNames;
    private ArrayList<String> artistName;
    private ArrayList<String> songLength;

    private MusicCloudModel(){
        songNames = new ArrayList<String>();
        artistName = new ArrayList<String>();
        songLength = new ArrayList<String>();
        initTest();
    }

    private void initTest(){
        songNames.add("Little Lion Man");
        songNames.add("Adagio For Strings");

        artistName.add("Mumford and Sons");
        artistName.add("Dj Tiesto");

        songLength.add("1:00");
        songLength.add("2:00");
    }

    public static MusicCloudModel getModel(){
        if (model == null){
            model = new MusicCloudModel();
        }
        return model;
    }

    public String getSongName(int pos){
        return songNames.get(pos);
    }

    public String getArtistName(int pos){
        return artistName.get(pos);
    }

    public String getSongLength(int pos){
        return songLength.get(pos);
    }

    public int getCount(){
        return songLength.size();
    }
}
