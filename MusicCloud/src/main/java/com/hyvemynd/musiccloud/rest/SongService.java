package com.hyvemynd.musiccloud.rest;

import com.hyvemynd.musiccloud.dto.SongRequestDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class SongService extends RestService<SongRequestDto, SongResponseDto> {

    private String getSongUrl(){
        return BASE_URL + "/songs";
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
        return getResponseObject(response, new SongResponseDto().getClass());
    }
}
