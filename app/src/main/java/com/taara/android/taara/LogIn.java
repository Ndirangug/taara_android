package com.taara.android.taara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.taara.android.taara.custom_objects.User;

public class LogIn extends AppCompatActivity {

    EditText mEmailOrPhone;
    EditText mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mEmailOrPhone = findViewById(R.id.edtEmailLogIn);
        mPassword = findViewById(R.id.edtPasswordLogin);
    }

    public void showAccountInfo(View view) {
        //executes log in and proceeds to AccountInfo.java
        User user = new User(getApplicationContext());
       boolean confirm = user.logIn(mEmailOrPhone.getText().toString(), mPassword.getText().toString());
        Log.i("return confirm", String.valueOf(confirm));

    }

    public void signUpInstead(View view) {
        //handler for 'sign up instead' option on log in screen
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}
