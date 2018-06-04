package com.taara.android.taara;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    static boolean needToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        try {
            needToLogin = intent.getBooleanExtra("LOGIN", false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (needToLogin) {
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
                                    Intent intent = new Intent(getApplicationContext(), RecentActivity.class);
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
                needToLogin = false;
                Intent intent = new Intent(getApplicationContext(), RecentActivity.class);
                startActivity(intent);
            }

        }


        mEmailOrPhone = findViewById(R.id.edtEmailLogIn);
        mPassword = findViewById(R.id.edtPasswordLogin);
    }

    public void showAccountInfo(View view) {
        //executes log in and proceeds to AccountInfo.java
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
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
                            Intent intent = new Intent(getApplicationContext(), RecentActivity.class);
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

        if (!(null == networkInfo)) {
            //handler for 'sign up instead' option on log in screen
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);

        } else {
            Snackbar.make(mEmailOrPhone.getRootView(), "Check your connection and try again", Snackbar.LENGTH_LONG).show();
        }

    }

    public void forgotPassword(View view) {
        String email = mEmailOrPhone.getText().toString();
        if (email.contains("@")) {
            getSharedPreferences("PASSWORD_RESET", Context.MODE_PRIVATE).edit().putString("EMAIL", email).commit();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(mEmailOrPhone.getRootView(), "Check your email for reset link", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            Snackbar.make(mEmailOrPhone.getRootView(), "Enter a valid email in the email field and click forgot password", Snackbar.LENGTH_SHORT).show();
        }
    }
}
