package com.taara.android.taara;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class AccountInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        SharedPreferences sharedPreferences = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        Toast.makeText(getApplicationContext(), sharedPreferences.getString("FIRST_NAME", "default") + " logged in", Toast.LENGTH_LONG).show();
    }
}
