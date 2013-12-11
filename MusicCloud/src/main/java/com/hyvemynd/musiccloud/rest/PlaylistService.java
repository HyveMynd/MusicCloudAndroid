package com.hyvemynd.musiccloud.rest;

import com.hyvemynd.musiccloud.dto.PlaylistRequestDto;
import com.hyvemynd.musiccloud.dto.PlaylistResponseDto;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Created by andresmonroy on 12/11/13.
 */
public class PlaylistService extends RestService<PlaylistRequestDto, PlaylistResponseDto> {

    private String getPlaylistUrl(){
        return BASE_URL + "/playlists";
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
