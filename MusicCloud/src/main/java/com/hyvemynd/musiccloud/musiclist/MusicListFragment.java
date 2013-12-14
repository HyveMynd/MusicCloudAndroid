package com.hyvemynd.musiccloud.musiclist;

import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicListFragment extends ListFragment implements RequestCallback {
    private Button addButtonView;
    private MusicCloudModel model;
    private OnSongSelectedListener onSongSelectedListener;
    private static final int SONG_PICK_ACTION = 10;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButtons();
//        getListView().addFooterView(addButtonView);
        model = ((MusicCloudApplication) getActivity().getApplication()).getModel();

        if(model.getAllSongs().size() != 0){
            setListAdapter(new MusicListAdapter(getActivity(), model.getAllSongs()));
        } else {
            model.requestAllSongs(this);
        }
    }

    private void initButtons(){
        addButtonView = new Button(getActivity());
        addButtonView.setText("Add");
        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMusic();
            }
        });
    }

    private void addMusic(){
        addButtonView.setText("YAY!");
        addSongToLibrary();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        onSongSelectedListener.onSongSelected(position);
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setListAdapter(null);
    }

    private void addSongToLibrary(){
        Uri uri;
        if (isSdPresent()){
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        }
        Intent i = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(i, SONG_PICK_ACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SONG_PICK_ACTION){
            Uri uri = data.getData();
            Log.e("musiclist", uri.getPath());
            model.addSong(getActivity().getContentResolver(), uri, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean isSdPresent()
    {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onDataReceived(Object result) {
    }

    @Override
    public void onModelChanged() {
        setListAdapter(new MusicListAdapter(getActivity(), model.getAllSongs()));
    }

    @Override
    public void onRequestFail(String message) {
        addButtonView.setText(message);
    }

    public OnSongSelectedListener getOnSongSelectedListener() {
        return onSongSelectedListener;
    }

    public void setOnSongSelectedListener(OnSongSelectedListener onSongSelectedListener) {
        this.onSongSelectedListener = onSongSelectedListener;
    }
}
