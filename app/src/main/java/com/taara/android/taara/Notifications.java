package com.taara.android.taara;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }

    public void notificationsOnClick(View view) {

        switch (view.getId()) {
            case R.id.notificationsBack:
                onBackPressed();
                break;


        }

    }
}
