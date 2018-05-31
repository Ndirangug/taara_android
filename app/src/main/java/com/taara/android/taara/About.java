package com.taara.android.taara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.facebookLayout || view.getId() == R.id.txtFacebook || view.getId() == R.id.fbIcon) {
            String url = "https://facebook.com/taara";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

        } else if (view.getId() == R.id.twitter || view.getId() == R.id.twitterLayout || view.getId() == R.id.txtTwittr) {
            String url = "https://twitter.com/taara";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else if (view.getId() == R.id.aboutBack) {
            onBackPressed();
        }
    }
}
