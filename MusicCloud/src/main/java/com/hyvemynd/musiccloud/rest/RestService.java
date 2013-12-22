package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hyvemynd.musiccloud.rest.callback.OnDeleteCallback;
import com.hyvemynd.musiccloud.rest.callback.OnGetCallback;
import com.hyvemynd.musiccloud.rest.callback.OnPostCallback;
import com.hyvemynd.musiccloud.rest.callback.OnPutCallback;

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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Created by Andres on 12/10/13.
 */
public abstract class RestService<RequestDto, ResponseDto> {
    protected static final String BASE_URL = "http://hyvemynd.org";
    protected static final String JSON_TYPE = "application/json";

    protected HttpClient client;

    public RestService(){
        client = new DefaultHttpClient();
    }

    public void createObject(RequestDto dto, OnPostCallback callback){
        PostTask task = new PostTask(callback);
        task.execute(dto);
    }

    public void updateObject(RequestDto dto, OnPutCallback callback){
        PutTask task = new PutTask(callback);
        task.execute(dto);
    }

    public void deleteObject(int id, OnDeleteCallback callback){
        DeleteTask task = new DeleteTask(callback);
        task.execute(id);
    }

    public void getObject(String identifier, OnGetCallback<ResponseDto> callback){
        GetTask task = new GetTask(callback);
        task.execute(identifier);
    }

    protected abstract String getPostUrl();
    protected abstract String getPutUrl();
    protected abstract String getDeleteUrl();
    protected abstract String getGetUrl();
    protected abstract HttpParams getGetParam(String identifier);

    protected int post(RequestDto dto) throws IOException{
        StringEntity se = getJsonEntity(dto);
        HttpPost request = getHttpPost(getPostUrl(), se);
        HttpResponse response = client.execute(request);
        return getResponseObject(response, int.class);
    }

    protected boolean put(RequestDto dto) throws IOException{
        StringEntity se = getJsonEntity(dto);
        HttpPut request = getHttpPut(getPutUrl(), se);
        HttpResponse response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    protected boolean delete(int id) throws IOException{
        HttpDelete request = getHttpDelete(getDeleteUrl(), id);
        HttpResponse response = client.execute(request);
        return getResponseObject(response, Boolean.class);
    }

    protected ResponseDto getEntity(String identifier) throws IOException{
        HttpGet request = new HttpGet(getGetUrl());
        request.setParams(getGetParam(identifier));
        HttpResponse response = client.execute(request);
        return getResponseObject(response);
    }

    private boolean isGoodResponse(HttpResponse response){
        return response.getStatusLine().getStatusCode() == 200;
    }

    protected abstract <T> T getResponseObject(HttpResponse response) throws IOException;

    protected <T> T getResponseObject(HttpResponse response, Class<T> responseType) throws IOException {
        if (isGoodResponse(response)){
            JsonReader reader = null;
            try{
                reader = new JsonReader(new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8")));
                return new Gson().fromJson(reader, responseType);
            } finally {
                assert reader != null;
                reader.close();
            }
        } else {
            return null;
        }
    }

    protected <T> T getResponseObject(HttpResponse response, Type type) throws IOException {
        if (isGoodResponse(response)){
            JsonReader reader = null;
            try{
                reader = new JsonReader(new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "UTF-8")));
                return new Gson().fromJson(reader, type);
            } finally {
                assert reader != null;
                reader.close();
            }
        } else {
            return null;
        }
    }

    protected HttpPost getHttpPost(String url, HttpEntity entity) throws MalformedURLException {
        URI uri = URI.create(url);
        HttpPost post = new HttpPost(uri);
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
        HttpDelete delete = new HttpDelete(url + "?Id=" + id);
        delete.addHeader("Content-Type", JSON_TYPE);
        delete.addHeader("Accept", JSON_TYPE);
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
        private OnPostCallback callback;

        public PostTask(OnPostCallback callback){
            this.callback = callback;
        }

        @Override
        protected Integer doInBackground(RequestDto... params) {
            int id = 0;
            try {
                id = post(params[0]);
            } catch (IOException e) {
                Log.e("RestService", e.getMessage());
            }
            return id;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            callback.onPostSuccess(integer);
            super.onPostExecute(integer);
        }
    }

    protected class PutTask extends AsyncTask<RequestDto, Void, Boolean>{
        private OnPutCallback callback;

        public PutTask(OnPutCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(RequestDto... params) {
            boolean result = false;
            try{
                result = put(params[0]);
            } catch (IOException e) {
                Log.e("RestService", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            callback.onPutSuccess(aBoolean);
            super.onPostExecute(aBoolean);
        }
    }

    protected class DeleteTask extends AsyncTask<Integer, Void, Boolean>{
        private OnDeleteCallback callback;

        public DeleteTask(OnDeleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean result = false;
            try{
                result = delete(params[0]);
            } catch (IOException e) {
                Log.e("RestService", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            callback.onDeleteSuccess(aBoolean);
            super.onPostExecute(aBoolean);
        }
    }

    protected class GetTask extends AsyncTask<String, Void, ResponseDto>{
        private OnGetCallback<ResponseDto> callback;

        public GetTask(OnGetCallback<ResponseDto> callback) {
            this.callback = callback;
        }

        @Override
        protected ResponseDto doInBackground(String... params) {
            ResponseDto dto = null;
            try{
                dto = getEntity(params[0]);
            } catch (IOException e) {
                Log.e("RestService", e.getMessage());
            }
            return dto;
        }

        @Override
        protected void onPostExecute(ResponseDto responseDto) {
            callback.onGetSuccess(responseDto);
            super.onPostExecute(responseDto);
        }
    }
}
