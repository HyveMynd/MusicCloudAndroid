package com.hyvemynd.musiccloud.rest;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hyvemynd.musiccloud.dto.SongRequestDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetCallback;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public void getSongsForUser(final OnGetCallback<List<SongResponseDto>> callback){
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

    public void getSongData(final ContentResolver resolver, final Uri uri, final int songId, final OnPostCallback callback){
        AsyncTask<Void, Void, Void> postData = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                streamSong(resolver, uri, songId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callback.onPostSuccess(0);
                super.onPostExecute(aVoid);
            }
        };
        postData.execute(null);
    }

    private void streamSong(ContentResolver resolver, Uri uri, int songId){
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        Cursor cursor = null;
        String pathToOurFile; //"/data/file_to_send.mp3";
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = resolver.query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            pathToOurFile = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        String urlServer = String.format("http://hyvemynd.org/songs/%d", songId); //"http://192.168.1.1/handle_upload.php";


        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(urlServer);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if(!pathToOurFile.isEmpty()){

                FileBody filebodyVideo = new FileBody(new File(pathToOurFile));
                reqEntity.addPart("uploaded", filebodyVideo);
            }
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }

            Log.e("Response: ", s.toString());

        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }

    }
    public static String getSongDataUrl(int id){
        return BASE_URL + String.format("/songs/%d/raw", id);
    }

    @Override
    protected String getPostUrl() {
        return getSongUrl();
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
