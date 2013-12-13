package com.hyvemynd.musiccloud.musiclist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.rest.callback.RequestCallback;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicListFragment extends ListFragment implements RequestCallback {
    private Button addButtonView;
    private MusicCloudModel model;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButtons();
        getListView().addFooterView(addButtonView);
        model = ((MusicCloudApplication) getActivity().getApplication()).getModel();
        setListAdapter(new MusicListAdapter(getActivity(), model.getAllSongs()));
        model.requestAllSongs(this);
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
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setListAdapter(null);
    }

    @Override
    public void onDataReceived(Object result) {

    }

    @Override
    public void onModelChanged() {
        setListAdapter(new MusicListAdapter(getActivity(), model.getAllSongs()));
    }
}
