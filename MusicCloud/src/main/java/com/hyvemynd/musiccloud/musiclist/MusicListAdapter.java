package com.hyvemynd.musiccloud.musiclist;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyvemynd.musiccloud.MusicCloudModel;
import com.hyvemynd.musiccloud.dto.SongResponseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicListAdapter extends ArrayAdapter {
    private Context mContext;
    private static final int SONG_NAME_SIZE = 22;
    private static final int ARTIST_NAME_SIZE = 15;
    private static final int SONG_LENGTH_SIZE = 18;
    private List<SongResponseDto> songs;

    public MusicListAdapter(Context context, List<SongResponseDto> songs) {
        super(context, 0);
        this.mContext = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
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
        holder.songName.setText(songs.get(position).Name);
        holder.artistName.setText(songs.get(position).Artist);
        int total = songs.get(position).Seconds;
        int min = total / 60;
        int sec = total % 60;
        String length = Integer.toString(min) + ":" + Integer.toString(sec);
        holder.songLength.setText(length);
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

    public void setSongList(List<SongResponseDto> list){
        this.songs = list;
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
