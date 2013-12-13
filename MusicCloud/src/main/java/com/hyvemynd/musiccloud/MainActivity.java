package com.hyvemynd.musiccloud;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hyvemynd.musiccloud.musiclist.MusicListFragment;
import com.hyvemynd.musiccloud.musiclist.OnSongSelectedListener;
import com.hyvemynd.musiccloud.musiclist.SongItem;
import com.hyvemynd.musiccloud.musicplayer.MusicPlayerFragment;
import com.hyvemynd.musiccloud.playlist.PlaylistListFragment;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String MUSIC_LIST_TAG = "music_list_tag";
    private static final String PLAYLIST_TAG = "playlist_list_tag";
    private static final String MUSIC_PLAYER_TAG = "music_player_tag";
    private static final String SETTINGS_TAG = "settings_view_tag";

    private MusicCloudModel model;
    private LinearLayout mainLayout;
    private MusicListFragment musicListFragment;
    private PlaylistListFragment playlistFragment;
    private MusicPlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ((MusicCloudApplication)getApplication()).getModel();
        mainLayout = new LinearLayout(this);
        mainLayout.setId(42);
        setContentView(mainLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        initFragments();
    }

    private void initFragments(){
        LoginRegFragment loginRegFragment = new LoginRegFragment();
        musicListFragment = new MusicListFragment();
        musicListFragment.setOnSongSelectedListener(new OnSongSelectedListener() {
            @Override
            public void onSongSelected(int position) {
                replaceFragment(MUSIC_PLAYER_TAG, playerFragment);
                List<SongItem> songs = model.getPlaylist(true);
                playerFragment.setPlaylist(songs, position);
            }
        });
        playlistFragment = new PlaylistListFragment();
        playerFragment = new MusicPlayerFragment();

        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(mainLayout.getId(), loginRegFragment);
        txn.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void replaceFragment(String tag, Fragment frag){
        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.replace(mainLayout.getId(), frag, tag);
        txn.commit();
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_music_list:
                replaceFragment(MUSIC_LIST_TAG, musicListFragment);
                return true;
            case R.id.action_playlist_list:
                replaceFragment(PLAYLIST_TAG, playlistFragment);
                return true;
            case R.id.action_music_player:
                replaceFragment(MUSIC_PLAYER_TAG, playerFragment);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void registerUser(String firstname, String lastname, String email, String password, RequestCallback request){
        model.registerUser(firstname, lastname, email, password, request);
    }

    public void loginUser(String email, String password, RequestCallback callback){
        model.loginUser(email, password, callback);
    }

    public void loginSuccess(){
        replaceFragment(MUSIC_LIST_TAG, musicListFragment);
    }
}
