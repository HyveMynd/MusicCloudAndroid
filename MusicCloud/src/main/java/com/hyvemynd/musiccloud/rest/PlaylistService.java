package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.hyvemynd.musiccloud.dto.PlaylistRequestDto;
import com.hyvemynd.musiccloud.dto.PlaylistResponseDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
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
