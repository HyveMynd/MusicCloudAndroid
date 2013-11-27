package com.hyvemynd.musiccloud;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyvemynd.musiccloud.musiclist.MusicListFragment;

public class MainActivity extends Activity {
    private LinearLayout mainLayout;
    private MusicListFragment musicListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);
        initFragments();
//        initLogin();
    }

    private void initFragments(){
        FrameLayout mainFrag = new FrameLayout(this);
        mainFrag.setId(42);
        mainLayout.addView(mainFrag, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        musicListFragment = new MusicListFragment();
        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(mainFrag.getId(), musicListFragment);
        txn.commit();
    }

    private void initLogin(){
        LinearLayout loginMain = new LinearLayout(this);
        loginMain.setOrientation(LinearLayout.VERTICAL);

        // logo
        TextView logoView = new TextView(this);
        logoView.setText(R.string.app_name);
        logoView.setTextColor(Color.LTGRAY);
        logoView.setTextSize(28);

        //username
        EditText usernameInput = new EditText(this);
        usernameInput.setHint(R.string.username_hint);
        usernameInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        //password
        EditText passwordInput = new EditText(this);
        passwordInput.setHint(R.string.password_hint);
        usernameInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //login/reg buttons
        LinearLayout buttonsView = new LinearLayout(this);
        Button loginButton = new Button(this);
        Button registerButton = new Button(this);
        loginButton.setText(R.string.login);
        registerButton.setText(R.string.register);
        buttonsView.addView(loginButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonsView.addView(registerButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Add views
        loginMain.addView(logoView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loginMain.addView(usernameInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loginMain.addView(passwordInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loginMain.addView(buttonsView);
        mainLayout.addView(loginMain);
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
