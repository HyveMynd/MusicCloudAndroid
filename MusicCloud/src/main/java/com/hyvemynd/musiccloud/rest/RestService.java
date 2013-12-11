package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.dto.UserResponseDto;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Andres on 12/10/13.
 */
public class RestService {
    private static final String BASE_URL = "http://hyvemynd.org";
    private static final String JSON_TYPE = "application/json";
    private static final String USERS_URL = BASE_URL + "/users";

    private HttpClient client;
    private HttpResponse response;

    public RestService(){
        client = new DefaultHttpClient();
    }

    public boolean createUser(UserRequestDto dto){
        Boolean result = false;
        AsyncTask<UserRequestDto, Void, Boolean> postUserTask = new AsyncTask<UserRequestDto, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(UserRequestDto... params) {
                Boolean result = false;
                try {
                    result = postUser(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
        postUserTask.execute(dto);
        try {
            result = postUserTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean postUser(UserRequestDto dto) throws IOException {
        StringEntity se = getJsonEntity(dto);
        HttpPost request = getHttpPost(USERS_URL, se);
        response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    private boolean putUser(UserRequestDto dto) throws IOException {
        StringEntity se = getJsonEntity(dto);
        HttpPut request = getHttpPut(USERS_URL, se);
        response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    private boolean deleteUser(int id) throws IOException {
        HttpDelete request = getHttpDelete(USERS_URL, id);
        response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    private UserResponseDto getUser(String email) throws IOException {
        HttpGet request = new HttpGet(USERS_URL);
        request.setParams(new BasicHttpParams().setParameter("email", email));
        response = client.execute(request);
        return getResponseObject(response, UserResponseDto.class);
    }

    private <T> T getResponseObject(HttpResponse response, Class<T> responseType) throws IOException {
        Gson gson = new Gson();
        InputStream is = response.getEntity().getContent();
        InputStreamReader reader = new InputStreamReader(is);
        return gson.fromJson(reader, responseType);
    }

    private HttpPost getHttpPost(String url, HttpEntity entity){
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", JSON_TYPE);
        post.setEntity(entity);
        return post;
    }

    private HttpPut getHttpPut(String url, HttpEntity entity){
        HttpPut put = new HttpPut(url);
        put.setHeader("Accept", JSON_TYPE);
        put.setEntity(entity);
        return put;
    }

    private HttpDelete getHttpDelete(String url, int id){
        HttpDelete delete = new HttpDelete(url);
        delete.setHeader("Accept", JSON_TYPE);
        delete.setParams(new BasicHttpParams().setParameter("Id", id));
        return delete;
    }

    private StringEntity getJsonEntity(Object object) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        StringEntity se = new StringEntity(json);
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, JSON_TYPE));
        return se;
    }
}
