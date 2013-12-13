package com.hyvemynd.musiccloud.musicplayer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.hyvemynd.musiccloud.musiclist.SongItem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MusicPlayerFragment extends Fragment implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener{
    private LinearLayout mainLayout;
    private Button buttonPlayPause;
    private SeekBar seekBarProgress;
    private EditText editTextSongURL;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private Queue<SongItem> playlist;

    private final Handler handler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(getActivity());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
//        playlist = new LinkedList<SongItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return mainLayout;
    }

    /** This method initialise all the views in project*/
    private void initView() {
        buttonPlayPause = new Button(getActivity());
        buttonPlayPause.setId(20);
        buttonPlayPause.setText("Play");
        buttonPlayPause.setOnClickListener(this);

        seekBarProgress = new SeekBar(getActivity());
        seekBarProgress.setId(21);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        editTextSongURL = new EditText(getActivity());
        editTextSongURL.setId(22);
        editTextSongURL.setText("http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        mainLayout.addView(editTextSongURL, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(buttonPlayPause, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.addView(seekBarProgress, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == 20){
            SongItem song = playlist.poll();
            if (song != null){
                playSong(song);
            }
        }
    }

    private void playSong(SongItem song){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        try {
            mediaPlayer.setDataSource(song.getUrl()); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
            mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            buttonPlayPause.setText("Pause");
        }else {
            mediaPlayer.pause();
            buttonPlayPause.setText("Play");
        }

        primarySeekBarProgressUpdater();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == 21){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        buttonPlayPause.setText("Play");
        SongItem song = playlist.poll();
        if (song != null){
            playSong(song);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBarProgress.setSecondaryProgress(percent);
    }

    public void setPlaylist(List<SongItem> playlist, int start){
        Queue<SongItem> songs = new LinkedList<SongItem>();
        for(int i = start; i < playlist.size(); i++)
            songs.offer(playlist.get(i));
        for(int i = 0; i < start; i++)
            songs.offer(playlist.get(i));
        this.playlist = songs;
    }

    @Override
    public void onResume() {
        if (playlist.size() != 0){
            playSong(playlist.poll());
        }
        super.onResume();
    }
}
