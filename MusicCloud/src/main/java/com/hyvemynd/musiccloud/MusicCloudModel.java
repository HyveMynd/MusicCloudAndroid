package com.hyvemynd.musiccloud;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.hyvemynd.musiccloud.dto.PlaylistRequestDto;
import com.hyvemynd.musiccloud.dto.PlaylistResponseDto;
import com.hyvemynd.musiccloud.dto.SongRequestDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.musiclist.SongItem;
import com.hyvemynd.musiccloud.rest.PlaylistService;
import com.hyvemynd.musiccloud.rest.SongService;
import com.hyvemynd.musiccloud.rest.callback.OnDeleteCallback;
import com.hyvemynd.musiccloud.rest.callback.OnGetCallback;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;
import com.hyvemynd.musiccloud.rest.UserService;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicCloudModel {
    private static MusicCloudModel model;

    private String userEmail;
    private List<PlaylistResponseDto> allPlaylists;
    private List<SongResponseDto> allSongs;
    private List<SongResponseDto> currentPlaylist;

    private MusicCloudModel(){
        allSongs = new ArrayList<SongResponseDto>();
        allPlaylists = new ArrayList<PlaylistResponseDto>();
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
                if (result != 0){
                    callback.onDataReceived(result);
                } else {
                    callback.onRequestFail("Could not create user.");
                }
            }
        });
    }

    public void loginUser(String email, String password, final RequestCallback callback){
        UserService service = new UserService();
        userEmail = email;
        service.login(email, password, new OnGetCallback<Boolean>() {
            @Override
            public void onGetSuccess(Boolean result) {
                if (result){
                    callback.onDataReceived(result);
                } else {
                    callback.onRequestFail("Error logging in.");
                }
            }
        });
    }

    public void requestAllSongs(final RequestCallback callback){
        SongService service = new SongService(userEmail);
        service.getSongsForUser(new OnGetCallback<List<SongResponseDto>>() {
            @Override
            public void onGetSuccess(List<SongResponseDto> result) {
                if (result != null){
                    allSongs = result;
                    callback.onModelChanged();
                }
            }
        });
    }

    public void requestAllPlayLists(final RequestCallback callback){
        PlaylistService service = new PlaylistService();
        service.getPlaylistsForUser(userEmail, new OnGetCallback<List<PlaylistResponseDto>>() {
            @Override
            public void onGetSuccess(List<PlaylistResponseDto> result) {
                if (result != null) {
                    allPlaylists = result;
                    callback.onModelChanged();
                }
            }
        });
    }

    public void createPlaylist(String name, boolean isTemp, final RequestCallback callback){
        PlaylistService service = new PlaylistService();
        service.createObject(new PlaylistRequestDto(userEmail, name, isTemp), new OnPostCallback() {
            @Override
            public void onPostSuccess(int result) {
                if (result != 0){
                    requestAllPlayLists(callback);
                } else {
                    callback.onRequestFail("Error creating playlist");
                }
            }
        });
    }

    public void addSongToLibrary(final ContentResolver resolver, final Uri songUri, final RequestCallback callback){
        Cursor cursor =  resolver.query(songUri, null, null, null, null);
        if (cursor == null){
            callback.onRequestFail("No such file.");
        } else if (!cursor.moveToFirst()){
            callback.onRequestFail("No media.");
        } else {
            // get parameters from MediaStore
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleCol = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int lengthCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int sizeCol = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            String title = cursor.getString(titleCol);
            String artist = cursor.getString(artCol);
            String album = cursor.getString(albCol);
            int size = (int)((cursor.getLong(sizeCol) / 1024) / 1024);
            int seconds = (int)(cursor.getLong(lengthCol) / 1000);

            SongRequestDto dto = new SongRequestDto(title, artist, album, seconds, size, userEmail);
            final SongService service = new SongService(userEmail);
            service.createObject(dto, new OnPostCallback() {
                @Override
                public void onPostSuccess(int result) {
                    service.getSongData(resolver, songUri, result, new OnPostCallback() {
                        @Override
                        public void onPostSuccess(int result) {
                            if (result != 0){
                                requestAllSongs(callback);
                            } else {
                                callback.onRequestFail("Error adding song");
                            }
                        }
                    });
                }
            });
        }
    }

    public List<SongItem> getPlaylistForPlayer(int playlistId, boolean isEntireLibrary){
        List<SongItem> songs = new ArrayList<SongItem>();
        if (isEntireLibrary){
            for(SongResponseDto s : allSongs){
                String url = SongService.getSongDataUrl(s.Id);
                songs.add(new SongItem(s.Name, s.Artist, s.Album, url));
            }
        } else {
            for(SongResponseDto s : currentPlaylist){
                String url = SongService.getSongDataUrl(s.Id);
                songs.add(new SongItem(s.Name, s.Artist, s.Album, url));
            }
        }
        return songs;
    }

    public void addSongToPlaylist(int songId, final RequestCallback callback){

    }

    public void removeSongFromPlaylist(int songId, final RequestCallback callback){

    }

    public void removePlaylist(final int playlistPosition, final RequestCallback callback){
        PlaylistResponseDto pl = allPlaylists.get(playlistPosition);
        PlaylistService service = new PlaylistService();
        service.deleteObject(pl.Id, new OnDeleteCallback() {
            @Override
            public void onDeleteSuccess(boolean result) {
                if (result){
                    allPlaylists.remove(playlistPosition);
                    callback.onModelChanged();
                } else {
                    callback.onRequestFail("Could not delete playlist");
                }
            }
        });
    }

    public void getSongsForPlaylist(final int playlistPosition, final RequestCallback callback){
        PlaylistService service = new PlaylistService();
        PlaylistResponseDto pl = allPlaylists.get(playlistPosition);
        service.getSongsForPlaylist(pl.Id, new OnGetCallback<List<SongResponseDto>>() {
            @Override
            public void onGetSuccess(List<SongResponseDto> result) {
                currentPlaylist = result;
                callback.onDataReceived(currentPlaylist);
            }
        });
    }

    public List<PlaylistResponseDto> getAllPlayLists(){
        return allPlaylists;
    }

    public List<SongResponseDto> getAllSongs() {
        return allSongs;
    }

    public List<SongResponseDto> getCurrentPlaylist(){
        return currentPlaylist;
    }
}
