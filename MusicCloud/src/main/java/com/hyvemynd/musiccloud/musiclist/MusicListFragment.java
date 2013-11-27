package com.hyvemynd.musiccloud.musiclist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hyvemynd.musiccloud.MusicCloudApplication;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new MusicListAdapter(getActivity(), ((MusicCloudApplication)getActivity().getApplication()).getModel()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
