package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.hyvemynd.musiccloud.dto.PlaylistRequestDto;
import com.hyvemynd.musiccloud.dto.PlaylistResponseDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnDeleteCallback;
import com.hyvemynd.musiccloud.rest.callback.OnGetCallback;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class PlaylistService extends RestService<PlaylistRequestDto, PlaylistResponseDto> {

    private String getPlaylistUrl(){
        return BASE_URL + "/playlists";
    }

    public void getPlaylistsForUser(final String email, final OnGetCallback<List<PlaylistResponseDto>> callback){
        AsyncTask<Void, Void, List<PlaylistResponseDto>> getTask = new AsyncTask<Void, Void, List<PlaylistResponseDto>>() {
            @Override
            protected List<PlaylistResponseDto> doInBackground(Void... params) {
                List<PlaylistResponseDto> result = null;
                HttpGet request = new HttpGet(BASE_URL + String.format("/users/%s/playlists", email));
                request.setHeader("Accept", JSON_TYPE);
                try{
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, new TypeToken<List<PlaylistResponseDto>>(){}.getType());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<PlaylistResponseDto> playlistResponseDtos) {
                callback.onGetSuccess(playlistResponseDtos);
                super.onPostExecute(playlistResponseDtos);
            }
        };
        getTask.execute(null);
    }

    public void getSongsForPlaylist(final int playlistId, final OnGetCallback<List<SongResponseDto>> callback){
        AsyncTask<Void, Void, List<SongResponseDto>> getTask = new AsyncTask<Void, Void, List<SongResponseDto>>() {
            @Override
            protected List<SongResponseDto> doInBackground(Void... params) {
                List<SongResponseDto> result = new ArrayList<SongResponseDto>();
                HttpGet request = new HttpGet(getGetUrl() + String.format("/%d/songs", playlistId));
                request.setHeader("Accept", JSON_TYPE);
                try{
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, new TypeToken<List<SongResponseDto>>(){}.getType());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<SongResponseDto> songResponseDtos) {
                callback.onGetSuccess(songResponseDtos);
                super.onPostExecute(songResponseDtos);
            }
        };
        getTask.execute();
    }

    public void addSongToPlaylist(final int playlistId, final int songId, final OnPostCallback callback){
        AsyncTask<Void, Void, Boolean> postTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                boolean result = false;
                HttpPost request = new HttpPost(getPostUrl() + String.format("/%d/songs/%d", playlistId, songId));
                request.setHeader("Accept", JSON_TYPE);
                try{
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, Boolean.class);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean){
                    callback.onPostSuccess(1);
                } else {
                    callback.onPostSuccess(0);
                }
                super.onPostExecute(aBoolean);
            }
        };
        postTask.execute();
    }

    public void removeSongFromPlaylist(final int playlistId, final int songId, final OnDeleteCallback callback){
        AsyncTask<Void, Void, Boolean> deleteTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                boolean result = false;
                HttpDelete request = new HttpDelete(getDeleteUrl() + String.format("/%d/songs/%d", playlistId, songId));
                request.setHeader("Accept", JSON_TYPE);
                try{
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, Boolean.class);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                callback.onDeleteSuccess(aBoolean);
                super.onPostExecute(aBoolean);
            }
        } ;
        deleteTask.execute();
    }

    @Override
    protected String getPostUrl() {
        return getPlaylistUrl();
    }

    @Override
    protected String getPutUrl() {
        return getPlaylistUrl();
    }

    @Override
    protected String getDeleteUrl() {
        return getPlaylistUrl();
    }

    @Override
    protected String getGetUrl() {
        return getPlaylistUrl();
    }

    @Override
    protected HttpParams getGetParam(String identifier) {
        return new BasicHttpParams().setParameter("Id", identifier);
    }

    @Override
    protected PlaylistResponseDto getResponseObject(HttpResponse response) throws IOException {
        return getResponseObject(response, new PlaylistResponseDto().getClass());
    }
}
