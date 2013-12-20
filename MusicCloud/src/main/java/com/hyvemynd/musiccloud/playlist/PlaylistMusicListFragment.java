package com.hyvemynd.musiccloud.playlist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hyvemynd.musiccloud.MainActivity;
import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.musiclist.MusicListAdapter;
import com.hyvemynd.musiccloud.musiclist.OnSongSelectedListener;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

/**
 * Created by andresmonroy on 12/14/13.
 */
public class PlaylistMusicListFragment extends ListFragment implements RequestCallback {
    private MusicCloudModel model;
    private OnSongSelectedListener onSongSelectedListener;
    private Button addButton;
    private Button removeButton;
    private boolean isRemoving;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isRemoving = false;
        initViews();
        getListView().addFooterView(addButton);
        getListView().addFooterView(removeButton);
        model = ((MusicCloudApplication)getActivity().getApplication()).getModel();
        setListAdapter(new MusicListAdapter(getActivity(), model.getCurrentPlaylist()));
    }

    private void initViews(){
        addButton = new Button(getActivity());
        addButton.setText("Add");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showMusicListForAddPlaylist();
            }
        });
        removeButton = new Button(getActivity());
        removeButton.setText("Remove");
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRemoving){
                    removeSong();
                } else {
                    openRemoveDialog();
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (isRemoving){
            model.removeSongFromPlaylist(position, this);
            closeRemoveDialog();
        } else {
            onSongSelectedListener.onSongSelected(position);
        }
    }

    private void removeSong(){
        closeRemoveDialog();
    }

    private void openRemoveDialog(){
        isRemoving = true;
        addButton.setVisibility(View.GONE);
        removeButton.setText("Cancel");
    }

    private void closeRemoveDialog(){
        isRemoving = false;
        addButton.setVisibility(View.VISIBLE);
        removeButton.setText("Remove");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setListAdapter(null);
    }

    public OnSongSelectedListener getOnSongSelectedListener() {
        return onSongSelectedListener;
    }

    public void setOnSongSelectedListener(OnSongSelectedListener onSongSelectedListener) {
        this.onSongSelectedListener = onSongSelectedListener;
    }

    @Override
    public void onDataReceived(Object result) {

    }

    @Override
    public void onModelChanged() {
        setListAdapter(new MusicListAdapter(getActivity(), model.getCurrentPlaylist()));
    }

    @Override
    public void onRequestFail(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
    }
}
