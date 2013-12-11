package com.hyvemynd.musiccloud.rest;

import android.os.AsyncTask;

import com.hyvemynd.musiccloud.dto.UserRequestDto;
import com.hyvemynd.musiccloud.dto.UserResponseDto;

import org.apache.http.HttpResponse;
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
        return getResponseObject(response, new UserResponseDto().getClass());
    }
}
