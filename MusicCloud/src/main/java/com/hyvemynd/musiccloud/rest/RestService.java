package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.google.gson.Gson;
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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Andres on 12/10/13.
 */
public abstract class RestService<RequestDto, ResponseDto> {
    protected static final String BASE_URL = "http://hyvemynd.org";
    protected static final String JSON_TYPE = "application/json";

    protected HttpClient client;
    protected HttpResponse response;

    public RestService(){
        client = new DefaultHttpClient();
    }

    public int createObject(RequestDto dto){
        int id = 0;
        PostTask task = new PostTask();
        task.execute(dto);
        try {
            id = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public boolean updateObject(RequestDto dto){
        boolean result = false;
        PutTask task = new PutTask();
        task.execute(dto);
        try {
            result = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteObject(int id){
        boolean result = false;
        DeleteTask task = new DeleteTask();
        task.execute(id);
        try {
            result = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ResponseDto getObject(String identifier){
        ResponseDto result = null;
        GetTask task = new GetTask();
        task.execute(identifier);
        try {
            result = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected abstract String getPostUrl();
    protected abstract String getPutUrl();
    protected abstract String getDeleteUrl();
    protected abstract String getGetUrl();
    protected abstract HttpParams getGetParam(String identifier);

    protected int post(RequestDto dto) throws IOException{
        StringEntity se = getJsonEntity(dto);
        HttpPost request = getHttpPost(getPostUrl(), se);
        response = client.execute(request);
        return getResponseObject(response, int.class);
    }

    protected boolean put(RequestDto dto) throws IOException{
        StringEntity se = getJsonEntity(dto);
        HttpPut request = getHttpPut(getPutUrl(), se);
        response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    protected boolean delete(int id) throws IOException{
        HttpDelete request = getHttpDelete(getDeleteUrl(), id);
        response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    protected ResponseDto getEntity(String identifier) throws IOException{
        HttpGet request = new HttpGet(getGetUrl());
        request.setParams(getGetParam(identifier));
        response = client.execute(request);
        return getResponseObject(response);

    }

    protected abstract ResponseDto getResponseObject(HttpResponse response) throws IOException;

    protected <T> T getResponseObject(HttpResponse response, Class<T> responseType) throws IOException {
        Gson gson = new Gson();
        InputStream is = response.getEntity().getContent();
        InputStreamReader reader = new InputStreamReader(is);
        return gson.fromJson(reader, responseType);
    }

    protected HttpPost getHttpPost(String url, HttpEntity entity){
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", JSON_TYPE);
        post.setEntity(entity);
        return post;
    }

    protected HttpPut getHttpPut(String url, HttpEntity entity){
        HttpPut put = new HttpPut(url);
        put.setHeader("Accept", JSON_TYPE);
        put.setEntity(entity);
        return put;
    }

    protected HttpDelete getHttpDelete(String url, int id){
        HttpDelete delete = new HttpDelete(url);
        delete.setHeader("Accept", JSON_TYPE);
        delete.setParams(new BasicHttpParams().setParameter("Id", id));
        return delete;
    }

    protected StringEntity getJsonEntity(Object object) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        StringEntity se = new StringEntity(json);
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, JSON_TYPE));
        return se;
    }

    protected class PostTask extends AsyncTask<RequestDto, Void, Integer>
    {
        @Override
        protected Integer doInBackground(RequestDto... params) {
            int id = 0;
            try {
                id = post(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }
    }

    protected class PutTask extends AsyncTask<RequestDto, Void, Boolean>{

        @Override
        protected Boolean doInBackground(RequestDto... params) {
            boolean result = false;
            try{
                result = put(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    protected class DeleteTask extends AsyncTask<Integer, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean result = false;
            try{
                result = delete(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    protected class GetTask extends AsyncTask<String, Void, ResponseDto>{

        @Override
        protected ResponseDto doInBackground(String... params) {
            ResponseDto dto = null;
            try{
                dto = getEntity(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dto;
        }
    }
}
