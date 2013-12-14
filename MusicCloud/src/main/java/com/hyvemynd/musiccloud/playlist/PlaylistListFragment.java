package com.hyvemynd.musiccloud.playlist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.musiclist.OnSongSelectedListener;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class PlaylistListFragment extends ListFragment implements OnSongSelectedListener, RequestCallback {
    private MusicCloudModel model;
    private Button addPlaylistButton;
    private Button removePlaylistButton;
    private EditText playlistEditText;
    private boolean isCreating;
    private boolean isRemoving;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        isCreating = false;
        isRemoving = false;
        getListView().addHeaderView(playlistEditText);
        getListView().addFooterView(addPlaylistButton);
        getListView().addFooterView(removePlaylistButton);
        model = ((MusicCloudApplication) getActivity().getApplication()).getModel();

        if(model.getAllPlayLists().size() != 0)
            setListAdapter(new PlaylistListAdapter(getActivity(), model.getAllPlayLists()));
        else {
            model.requestAllPlayLists(this);
        }
    }

    private void initViews() {
        addPlaylistButton = new Button(getActivity());
        addPlaylistButton.setText("Add");
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreating){
                    createPlaylist();
                } else {
                    openCreateDialog();
                }
            }
        });
        removePlaylistButton = new Button(getActivity());
        removePlaylistButton.setText("Remove");
        removePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreating){
                    closeCreateDialog();
                } else {
                    if (isRemoving){
                        removePlaylist();
                    } else {
                        openRemoveDialog();
                    }
                }
            }
        });
        playlistEditText = new EditText(getActivity());
        playlistEditText.setHint("Enter Playlist Name");
        playlistEditText.setVisibility(View.GONE);
    }

    private void createPlaylist(){
        closeCreateDialog();
        model.createPlaylist(playlistEditText.getText().toString(), false, this);
    }

    private void openCreateDialog(){
        isCreating = true;
        playlistEditText.setVisibility(View.VISIBLE);
        addPlaylistButton.setText("Create");
        removePlaylistButton.setText("Cancel");
    }

    private void removePlaylist(){
        removePlaylistButton.setText("Remove");
        addPlaylistButton.setVisibility(View.VISIBLE);
        isRemoving = false;
    }

    private void openRemoveDialog(){
        removePlaylistButton.setText("Cancel");
        addPlaylistButton.setVisibility(View.GONE);
        isRemoving = true;
    }

    private void closeCreateDialog(){
        isCreating = false;
        playlistEditText.setVisibility(View.GONE);
        addPlaylistButton.setText("Add");
        removePlaylistButton.setText("Remove");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onSongSelected(int position) {

    }

    @Override
    public void onDataReceived(Object result) {

    }

    @Override
    public void onModelChanged() {
        ((PlaylistListAdapter)getListAdapter()).setList(model.getAllPlayLists());
        ((PlaylistListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onRequestFail(String message) {

    }
}
