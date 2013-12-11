package com.hyvemynd.musiccloud;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyvemynd.musiccloud.musiclist.MusicListFragment;

public class MainActivity extends Activity {
    private LinearLayout mainLayout;
    private MusicListFragment musicListFragment;
    private LoginRegFragment loginRegFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setId(42);
        setContentView(mainLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        initFragments();
    }

    private void initFragments(){
        loginRegFragment = new LoginRegFragment();
        musicListFragment = new MusicListFragment();

        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(mainLayout.getId(), loginRegFragment);
        txn.add(mainLayout.getId(), musicListFragment);
//        txn.hide(musicListFragment);
        txn.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_music_list:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
