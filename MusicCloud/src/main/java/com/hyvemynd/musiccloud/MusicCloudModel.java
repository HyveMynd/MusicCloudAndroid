package com.hyvemynd.musiccloud;

import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetSuccessCallback;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;
import com.hyvemynd.musiccloud.rest.UserService;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import java.util.ArrayList;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicCloudModel {
    private static MusicCloudModel model;

    private String userEmail;

    private ArrayList<String> songNames;
    private ArrayList<String> artistName;
    private ArrayList<String> songLength;
    private ArrayList<String> playlistNames;
    private ArrayList<String> playlistItems;

    private MusicCloudModel(){
        songNames = new ArrayList<String>();
        artistName = new ArrayList<String>();
        songLength = new ArrayList<String>();
        playlistNames = new ArrayList<String>();
        playlistItems = new ArrayList<String>();
        initTest();
    }

    private void initTest(){
        songNames.add("Little Lion Man");
        songNames.add("Adagio For Strings");

        artistName.add("Mumford and Sons");
        artistName.add("Dj Tiesto");

        songLength.add("1:00");
        songLength.add("2:00");

        playlistNames.add("Mix 1");
        playlistNames.add("Mix 2");

        playlistItems.add("13");
        playlistItems.add("18");
    }

    public static MusicCloudModel getModel(){
        if (model == null){
            model = new MusicCloudModel();
        }
        return model;
    }

    public void registerUser(String firstName, String lastName, String email, String password, final RequestCallback callback){
        UserService service = new UserService();
        UserRequestDto dto = new UserRequestDto(firstName, lastName, email, password);
        userEmail = email;
        service.createObject(dto, new OnPostCallback() {
            @Override
            public void onPostSuccess(int result) {
                callback.onDataRecieved(result);
            }
        });
    }

    public void loginUser(String email, String password, final RequestCallback callback){
        UserService service = new UserService();
        userEmail = email;
        service.login(email, password, new OnGetSuccessCallback<Boolean>() {
            @Override
            public void onGetSuccess(Boolean result) {
                callback.onDataRecieved(result);
            }
        });
    }

    public void getAllSongs(){
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

    public String getPlaylistName(int pos){
        return playlistNames.get(pos);
    }

    public String getPlaylistItems(int pos){
        return playlistItems.get(pos);
    }

    public String getUserEmail() {
        return userEmail;
    }
}
