package com.taara.android.taara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.taara.android.taara.custom_objects.User;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogIn";
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
        final User user = new User(getApplicationContext());
        user.logIn(mEmailOrPhone.getText().toString(), mPassword.getText().toString());
        Thread delay = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    if (user.getLogIn()) {
                        Log.i(TAG, "Log in successfull  ");
                        Intent intent = new Intent(getApplicationContext(), AccountInformation.class);
                        startActivity(intent);
                    } else {
                        Log.i(TAG, "Log in failed  ");
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        delay.start();




    }

    public void signUpInstead(View view) {
        //handler for 'sign up instead' option on log in screen
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}
