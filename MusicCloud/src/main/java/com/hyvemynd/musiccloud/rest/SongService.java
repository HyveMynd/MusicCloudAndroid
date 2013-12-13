package com.hyvemynd.musiccloud.rest;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.hyvemynd.musiccloud.dto.SongRequestDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetSuccessCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, new TypeToken<List<SongResponseDto>>(){}.getType());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        getTask.execute(null);
    }

    public static String getSongDataUrl(int id){
        return BASE_URL + String.format("/songs/%d/raw", id);
    }

    public void playSongData(byte[] data, Activity activity){
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", activity.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(data);
            fos.close();

            // Tried reusing instance of media player
            // but that resulted in system crashes...
            MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }

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
