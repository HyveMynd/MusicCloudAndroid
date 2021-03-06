package com.hyvemynd.musiccloud.playlist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hyvemynd.musiccloud.MainActivity;
import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class PlaylistListFragment extends ListFragment implements RequestCallback {
    private MusicCloudModel model;
    private Button addPlaylistButton;
    private Button removePlaylistButton;
    private Button shareButton;
    private EditText playlistEditText;
    private boolean isCreating;
    private boolean isRemoving;
    private boolean isSharing;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        isCreating = false;
        isRemoving = false;
        isSharing = false;
        getListView().addHeaderView(playlistEditText);
        getListView().addFooterView(addPlaylistButton);
        getListView().addFooterView(removePlaylistButton);
        getListView().addFooterView(shareButton);
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
                if (isCreating) {
                    closeCreateDialog();
                } else {
                    if (isRemoving) {
                        removePlaylist();
                    } else {
                        openRemoveDialog();
                    }
                }
            }
        });
        playlistEditText = new EditText(getActivity());
        playlistEditText.setVisibility(View.GONE);
        shareButton = new Button(getActivity());
        shareButton.setText("Share");
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSharing){
                    closeShareDialog();
                } else {
                    openShareDialog();
                }
            }
        });
    }

    private void sharePlaylist(int playlistPos){
        closeShareDialog();
        String email = playlistEditText.getText().toString();
        model.sharePlaylist(email, playlistPos, this);
    }

    private void openShareDialog(){
        isSharing = true;
        playlistEditText.setVisibility(View.VISIBLE);
        playlistEditText.setHint("Enter friend's email");
        shareButton.setText("Cancel");
        addPlaylistButton.setVisibility(View.GONE);
        removePlaylistButton.setVisibility(View.GONE);
    }

    private void closeShareDialog(){
        isSharing = false;
        playlistEditText.setVisibility(View.GONE);
        shareButton.setText("Share");
        addPlaylistButton.setVisibility(View.VISIBLE);
        removePlaylistButton.setVisibility(View.VISIBLE);
    }

    private void createPlaylist(){
        closeCreateDialog();
        if (playlistEditText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT);
        } else {
            model.createPlaylist(playlistEditText.getText().toString(), false, this);
        }
    }

    private void openCreateDialog(){
        isCreating = true;
        playlistEditText.setVisibility(View.VISIBLE);
        playlistEditText.setHint("Enter Playlist Name");
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

    private void closeRemoveDialog(){
        removePlaylistButton.setText("Remove");
        addPlaylistButton.setVisibility(View.VISIBLE);
        isRemoving = false;
    }

    private void closeCreateDialog(){
        isCreating = false;
        playlistEditText.setVisibility(View.GONE);
        addPlaylistButton.setText("Add");
        removePlaylistButton.setText("Remove");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (isRemoving){
            model.removePlaylist(position-1, this);
            closeRemoveDialog();
        } else if (isSharing){
            sharePlaylist(position-1);
        } else {
            model.getSongsForPlaylist(position-1, this);
        }
    }

    @Override
    public void onDataReceived(Object result) {
        ((MainActivity)getActivity()).showPlaylist();
    }

    @Override
    public void onModelChanged() {
        setListAdapter(new PlaylistListAdapter(getActivity(), model.getAllPlayLists()));
    }

    @Override
    public void onRequestFail(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
    }
}
