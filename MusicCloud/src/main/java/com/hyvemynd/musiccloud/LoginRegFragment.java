package com.hyvemynd.musiccloud;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by andresmonroy on 11/30/13.
 */
public class LoginRegFragment extends Fragment {
    private LinearLayout contentLayout;
    private TextView logoView;
    private EditText firstnameView;
    private EditText lastnameView;
    private EditText usernameView;
    private EditText passwordConfirmView;
    private EditText passwordView;
    private LinearLayout buttonsLayout;
    private Button loginButton;
    private Button registerButton;
    private boolean isRegistering;

    private static final int LOGO_ID = 100;
    private static final int FIRST_NAME_ID = 101;
    private static final int LAST_NAME_ID = 102;
    private static final int USERNAME_ID = 103;
    private static final int PASSWORD_ID = 104;
    private static final int PASSWORD_CONF_ID = 105;
    private static final int BUTTONS_ID = 106;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contentLayout = new LinearLayout(activity);

        logoView = new TextView(activity);
        logoView.setId(LOGO_ID);
        firstnameView = new EditText(activity);
        firstnameView.setId(FIRST_NAME_ID);
        lastnameView = new EditText(activity);
        lastnameView.setId(LAST_NAME_ID);
        usernameView = new EditText(activity);
        usernameView.setId(USERNAME_ID);
        passwordView = new EditText(activity);
        passwordView.setId(PASSWORD_ID);
        passwordConfirmView = new EditText(activity);
        passwordConfirmView.setId(PASSWORD_CONF_ID);
        buttonsLayout = new LinearLayout(activity);
        buttonsLayout.setId(BUTTONS_ID);

        loginButton = new Button(activity);
        registerButton = new Button(activity);
        isRegistering = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentLayout.setOrientation(LinearLayout.VERTICAL);

        // logo
        logoView.setText(R.string.app_name);
        logoView.setTextColor(Color.LTGRAY);
        logoView.setTextSize(28);

        // firstname
        firstnameView.setHint(R.string.firstname_hint);
        firstnameView.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);

        // lastname
        lastnameView.setHint(R.string.lastname_hint);
        lastnameView.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);

        //username
        usernameView.setHint(R.string.username_hint);
        usernameView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        //password
        passwordView.setHint(R.string.password_hint);
        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // password confirm
        passwordConfirmView.setHint(R.string.pass_confirm);
        passwordConfirmView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //login/reg buttons
        loginButton.setText(R.string.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClick();
            }
        });
        registerButton.setText(R.string.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClick();
            }
        });
        LinearLayout.LayoutParams loginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams regParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonsLayout.addView(loginButton, loginParams);
        buttonsLayout.addView(registerButton, regParams);

        // Add views
        contentLayout.addView(logoView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        contentLayout.addView(firstnameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(lastnameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(usernameView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(passwordView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(passwordConfirmView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(buttonsLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        hideRegistration();
        return contentLayout;
    }

    private void showRegistration(){
        firstnameView.setVisibility(View.VISIBLE);
        lastnameView.setVisibility(View.VISIBLE);
        passwordConfirmView.setVisibility(View.VISIBLE);
        registerButton.setText(R.string.cancel);
        loginButton.setText(R.string.create);
        isRegistering = true;
    }

    private void hideRegistration(){
        firstnameView.setVisibility(View.GONE);
        lastnameView.setVisibility(View.GONE);
        passwordConfirmView.setVisibility(View.GONE);
        registerButton.setText(R.string.register);
        loginButton.setText(R.string.login);
        isRegistering = false;
    }

    private void loginUser(){

    }

    private void createUser(){
        if (passwordsMatch()){
            registerButton.setText("GOOD!");
        } else {
            registerButton.setText("BAD!!");
        }
    }

    private boolean passwordsMatch() {
        return passwordView.getText().toString().equals(passwordConfirmView.getText().toString());
    }

    private void loginButtonClick(){
        if (isRegistering){
            createUser();
        } else {
            loginUser();
        }
    }

    private void registerButtonClick(){
        if (isRegistering){
            hideRegistration();
        } else {
            showRegistration();
        }
    }
}
