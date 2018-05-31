package com.taara.android.taara;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taara.android.taara.custom_objects.User;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogIn";
    EditText mEmailOrPhone;
    EditText mPassword;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        boolean logIn = false;
        try {
            logIn = intent.getBooleanExtra("LOGIN", false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (logIn) {
            try {
                String newEmail = intent.getStringExtra("EMAIL");
                String newPassword = intent.getStringExtra("PASSWORD");
                if (networkInfo.isConnected()) {
                    final User user = new User(getApplicationContext());
                    user.logIn(newEmail, newPassword);
                    //TODO:PROGRESS ANIMATION
                    Thread delay = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(4000);
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


                } else {
                    Snackbar.make(mEmailOrPhone.getRootView(), "Check your connection and try again", Snackbar.LENGTH_LONG).show();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {

            if (!(user == null)) {
                Intent intent = new Intent(getApplicationContext(), AccountInformation.class);
                startActivity(intent);
            }

        }


        mEmailOrPhone = findViewById(R.id.edtEmailLogIn);
        mPassword = findViewById(R.id.edtPasswordLogin);
    }

    public void showAccountInfo(View view) {
        //executes log in and proceeds to AccountInfo.java


        if (networkInfo.isConnected()) {
            final User user = new User(getApplicationContext());
            user.logIn(mEmailOrPhone.getText().toString(), mPassword.getText().toString());
            //TODO:PROGRESS ANIMATION
            Thread delay = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(4000);
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


        } else {
            Snackbar.make(mEmailOrPhone.getRootView(), "Check your connection and try again", Snackbar.LENGTH_LONG).show();
        }


    }

    public void signUpInstead(View view) {


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (!(networkInfo == null)) {
            //handler for 'sign up instead' option on log in screen
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);

        } else {
            Snackbar.make(mEmailOrPhone.getRootView(), "Check your connection and try again", Snackbar.LENGTH_LONG).show();
        }

    }
}
