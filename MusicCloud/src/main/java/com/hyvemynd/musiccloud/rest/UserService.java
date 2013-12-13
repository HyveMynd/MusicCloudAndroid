package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.dto.UserResponseDto;
import com.hyvemynd.musiccloud.rest.callback.OnGetSuccessCallback;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Andres on 12/11/13.
 */
public class UserService extends RestService<UserRequestDto, UserResponseDto>{

    private String getUserUrl(){
        return BASE_URL + "/users";
    }

    public void login(String email, String password, final OnGetSuccessCallback<Boolean> callback){
        AsyncTask<String, Void, Boolean> loginTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                boolean result = false;
                String url = String.format(getUserUrl() + "/login/%s/%s", params[0], params[1]);
                HttpGet request = new HttpGet(url);
                request.setHeader("Accept", JSON_TYPE);
                try {
                    response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() == 200){
                        result = getResponseObject(response, Boolean.class);
                    }
                } catch (IOException e) {
                    Log.e("UserService", e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                callback.onGetSuccess(aBoolean);
                super.onPostExecute(aBoolean);
            }
        };
        loginTask.execute(email, password);
    }

    @Override
    protected String getPostUrl() {
        return getUserUrl();
    }

    @Override
    protected String getPutUrl() {
        return getUserUrl();
    }

    @Override
    protected String getDeleteUrl() {
        return getUserUrl();
    }

    @Override
    protected String getGetUrl() {
        return getUserUrl();
    }

    @Override
    protected HttpParams getGetParam(String identifier) {
        return new BasicHttpParams().setParameter("email", identifier);
    }

    @Override
    protected UserResponseDto getResponseObject(HttpResponse response) throws IOException {
        return getResponseObject(response, UserResponseDto.class);
    }
}
