package com.hyvemynd.musiccloud.musiclist;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyvemynd.musiccloud.MusicCloudModel;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicListAdapter extends ArrayAdapter {
    private MusicCloudModel mModel;
    private Context mContext;
    private static final int SONG_NAME_SIZE = 25;
    private static final int ARTIST_NAME_SIZE = 15;
    private static final int SONG_LENGTH_SIZE = 18;

    public MusicListAdapter(Context context, MusicCloudModel model) {
        super(context, 0);
        this.mModel = model;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mModel.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LinearLayout row = (LinearLayout)convertView;
        if (row == null){
            holder = new ViewHolder();
            row = new LinearLayout(mContext);
            row.setBackgroundColor(Color.BLACK);
            row.addView(createRowView(holder));
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.songName.setText(mModel.getSongName(position));
        holder.artistName.setText(mModel.getArtistName(position));
        holder.songLength.setText(mModel.getSongLength(position));
        return row;
    }

    private View createRowView(ViewHolder holder){
        LinearLayout row = new LinearLayout(mContext);
        row.setOrientation(LinearLayout.VERTICAL);

        // Set layout rules
        RelativeLayout.LayoutParams songNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        songNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams artistNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        artistNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams songLengthParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        songLengthParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // Add relative views
        RelativeLayout rel1 = new RelativeLayout(mContext);
        RelativeLayout rel2 = new RelativeLayout(mContext);
        rel1.addView(holder.songName, songNameParams);
        rel1.addView(holder.songLength, songLengthParams);
        rel2.addView(holder.artistName, artistNameParams);

        // add to row view
        row.addView(rel1);
        row.addView(rel2);
        return row;
    }

    class ViewHolder{
        TextView songName;
        TextView artistName;
        TextView songLength;

        public ViewHolder(){
            songName = new TextView(mContext);
            artistName = new TextView(mContext);
            songLength = new TextView(mContext);
            songName.setTextSize(SONG_NAME_SIZE);
            artistName.setTextSize(ARTIST_NAME_SIZE);
            songLength.setTextSize(SONG_LENGTH_SIZE);
        }
    }
}
