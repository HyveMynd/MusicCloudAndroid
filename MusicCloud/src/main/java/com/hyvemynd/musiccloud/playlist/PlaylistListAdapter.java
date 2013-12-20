package com.hyvemynd.musiccloud.playlist;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyvemynd.musiccloud.dto.PlaylistResponseDto;
import com.hyvemynd.musiccloud.dto.SongResponseDto;

import java.util.List;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class PlaylistListAdapter extends ArrayAdapter {
    private Context mContext;
    private List<PlaylistResponseDto> playLists;

    private static final int PLAYLIST_NAME_SIZE = 22;
    public static final int EXPR_SIZE = 14;
    private static final int ITEMS_SIZE = 18;


    public PlaylistListAdapter(Context context, List<PlaylistResponseDto> playlists) {
        super(context, 0);
        mContext = context;
        this.playLists = playlists;
    }

    public void setList(List<PlaylistResponseDto> list){
        playLists = list;
    }

    @Override
    public int getCount() {
        return playLists.size();
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
        holder.name.setText(playLists.get(position).Name);
        holder.items.setText(Integer.toString(playLists.get(position).Items));
        if (playLists.get(position).IsTemporary){
            holder.expr.setText(playLists.get(position).ExpirationDate);
        } else {
            holder.expr.setText("");
        }
        return row;
    }

    private View createRowView(ViewHolder holder){
        LinearLayout row = new LinearLayout(mContext);
        row.setOrientation(LinearLayout.HORIZONTAL);

        // Set layout rules
        RelativeLayout.LayoutParams playlistNameParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playlistNameParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams exprParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        exprParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams itemsParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemsParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout rel1 = new RelativeLayout(mContext);
        rel1.addView(holder.name, playlistNameParam);
        rel1.addView(holder.items, itemsParam);

        row.addView(rel1);
        return row;
    }

        class ViewHolder {
            private TextView name;
            private TextView expr;
            private TextView items;

            public ViewHolder(){
                name = new TextView(mContext);
                name.setTextSize(PLAYLIST_NAME_SIZE);
                expr = new TextView(mContext);
                expr.setTextSize(EXPR_SIZE);
                items = new TextView(mContext);
                items.setTextSize(ITEMS_SIZE);
            }
    }
}
