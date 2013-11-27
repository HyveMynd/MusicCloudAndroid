package com.hyvemynd.musiccloud.playlist;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.hyvemynd.musiccloud.MusicCloudModel;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class PlaylistListAdapter extends ArrayAdapter<PlayListItem> {
    private Context mContext;
    private MusicCloudModel mModel;

    public PlaylistListAdapter(Context context, MusicCloudModel model) {
        super(context, 0);
        mContext = context;
        mModel = model;
    }
}
