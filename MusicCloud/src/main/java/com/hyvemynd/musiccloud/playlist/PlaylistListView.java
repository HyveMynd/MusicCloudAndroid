package com.hyvemynd.musiccloud.playlist;

import android.app.ListFragment;
import android.os.Bundle;

import com.hyvemynd.musiccloud.MusicCloudApplication;
import com.hyvemynd.musiccloud.MusicCloudModel;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class PlaylistListView extends ListFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MusicCloudModel model = ((MusicCloudApplication)getActivity().getApplication()).getModel();
        setListAdapter(new PlaylistListAdapter(getActivity(), model));
    }
}
