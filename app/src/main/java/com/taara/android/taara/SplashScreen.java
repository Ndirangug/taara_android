package com.taara.android.taara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Thread that shows the splash screen for 3 seconds before proceeding to LogIn.class
        Thread splash = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                    Intent goToLOgIn = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(goToLOgIn);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        splash.start();




    }
}
