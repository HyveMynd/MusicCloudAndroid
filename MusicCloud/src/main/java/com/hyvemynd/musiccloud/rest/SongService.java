package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.hyvemynd.musiccloud.dto.SongRequestDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetSuccessCallback;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class SongService extends RestService<SongRequestDto, SongResponseDto> {
    private String userEmail;

    public SongService(String userEmail){
        this.userEmail = userEmail;
    }

    private String getSongUrl(){
        return BASE_URL + "/songs";
    }

    public void getSongsForUser(final OnGetSuccessCallback<List<SongResponseDto>> callback){
        AsyncTask<Void, Void, List<SongResponseDto>> getTask = new AsyncTask<Void, Void, List<SongResponseDto>>() {

            @Override
            protected void onPostExecute(List<SongResponseDto> songResponseDtos) {
                callback.onGetSuccess(songResponseDtos);
                super.onPostExecute(songResponseDtos);
            }

            @Override
            protected List<SongResponseDto> doInBackground(Void... params) {
                List<SongResponseDto> result = null;
                HttpGet request = new HttpGet(BASE_URL + String.format("/users/%s/songs", userEmail));
                request.setHeader("Accept", JSON_TYPE);
                try{
                    response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, new TypeToken<ArrayList<SongResponseDto>>(){});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        getTask.execute(null);
    }

    @Override
    protected String getPostUrl() {
        return getSongUrl() + String.format("/users/%s/songs", userEmail);
    }

    @Override
    protected String getPutUrl() {
        return getSongUrl();
    }

    @Override
    protected String getDeleteUrl() {
        return getSongUrl();
    }

    @Override
    protected String getGetUrl() {
        return getSongUrl();
    }

    @Override
    protected HttpParams getGetParam(String identifier) {
        return new BasicHttpParams().setParameter("Id", identifier);
    }

    @Override
    protected SongResponseDto getResponseObject(HttpResponse response) throws IOException {
        return getResponseObject(response, SongResponseDto.class);
    }
}
