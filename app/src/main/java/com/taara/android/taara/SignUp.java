package com.taara.android.taara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view) {
        //handler for 'SIGN UP' button
    }

    public void logInInstead(View view) {
        //handler for 'log in instead' option on log in screen
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
    }
}
