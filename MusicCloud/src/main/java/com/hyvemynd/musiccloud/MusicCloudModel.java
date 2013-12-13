package com.hyvemynd.musiccloud;

import com.hyvemynd.musiccloud.dto.SongDataResponseDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.rest.SongService;
import com.hyvemynd.musiccloud.rest.callback.OnGetSuccessCallback;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;
import com.hyvemynd.musiccloud.rest.UserService;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicCloudModel {
    private static MusicCloudModel model;

    private String userEmail;

    private ArrayList<String> playlistNames;
    private ArrayList<String> playlistItems;

    private List<SongResponseDto> allSongs;
    private Queue<byte[]> songData;

    private MusicCloudModel(){
        playlistNames = new ArrayList<String>();
        playlistItems = new ArrayList<String>();
        allSongs = new ArrayList<SongResponseDto>();
        songData = new LinkedList<byte[]>();
        initTest();
    }

    private void initTest(){
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
                callback.onDataReceived(result);
            }
        });
    }

    public void loginUser(String email, String password, final RequestCallback callback){
        UserService service = new UserService();
        userEmail = email;
        service.login(email, password, new OnGetSuccessCallback<Boolean>() {
            @Override
            public void onGetSuccess(Boolean result) {
                callback.onDataReceived(result);
            }
        });
    }

    public void requestAllSongs(final RequestCallback callback){
        SongService service = new SongService(userEmail);
        service.getSongsForUser(new OnGetSuccessCallback<List<SongResponseDto>>() {
            @Override
            public void onGetSuccess(List<SongResponseDto> result) {
                allSongs = result;
                callback.onModelChanged();
            }
        });
    }

    public void playSong(int position, boolean isFromMainLibrary, final RequestCallback callback){
        if (isFromMainLibrary){
            int id = allSongs.get(position).Id;
            SongService service = new SongService(userEmail);
            service.getSongData(id, new OnGetSuccessCallback<SongDataResponseDto>() {
                @Override
                public void onGetSuccess(SongDataResponseDto result) {
                    songData.add(result.Data.getBytes());
                    callback.onDataReceived(result.Data.getBytes());
                }
            });
        }
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

    public List<SongResponseDto> getAllSongs() {
        return allSongs;
    }
}
