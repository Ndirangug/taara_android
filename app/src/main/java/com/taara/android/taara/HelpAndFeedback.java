package com.taara.android.taara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taara.android.taara.custom_objects.RestApiCall;

public class HelpAndFeedback extends AppCompatActivity {

    String userEmail, userName;
    SharedPreferences sharedPreferences;
    EditText edtSubject, mEdtEmail, edtMessage;
    Spinner feedbackOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        sharedPreferences = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("EMAIL", null);
        userName = sharedPreferences.getString("FIRST_NAME", null) + " " + sharedPreferences.getString("SECOND_NAME", null);

        edtSubject = findViewById(R.id.feedback_subject);
        mEdtEmail = findViewById(R.id.feedback_email);
        edtMessage = findViewById(R.id.feedback_message);
        feedbackOptions = findViewById(R.id.feedback_options);

        mEdtEmail.setText(userEmail);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.feedback_options));
        feedbackOptions.setAdapter(adapter);

    }

    public void onClick(final View view) {
        if (view.getId() == R.id.feedbackBack) {
            onBackPressed();
        }

        if (view.getId() == R.id.feedback_send) {
            String subject = edtSubject.getText().toString();
            String category = feedbackOptions.getSelectedItem().toString();
            Log.i("URL", category);
            String message = "Name: " + userName + "\nEmail: " + mEdtEmail.getText().toString() + "\nMessage:\n" + edtMessage.getText().toString();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=contact&message=" + message + "&subject=" + subject + "&category=" + category;
            final RestApiCall restApiCall = new RestApiCall(getApplicationContext(), url.replaceAll("\n+", ",").replaceAll("\\s+", "_"));
            queue.add(restApiCall.makeJsonArrayRequest());
            RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
                @Override
                public void onRequestFinished(Request request) {
                    String[] result = restApiCall.getResponseArray();
                    try {
                        if (result[0].equals("completed")) {
                            Log.i("MAIL", "success");
                            Snackbar.make(view.getRootView(), "Message sent successfully", Snackbar.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(view.getRootView(), "Message not sent.Check your connection and try again", Snackbar.LENGTH_SHORT);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Snackbar.make(view.getRootView(), "Request error", Snackbar.LENGTH_SHORT);
                    }

                }
            };
            queue.addRequestFinishedListener(requestFinishedListener);
            //send email
        }

        if (view.getId() == R.id.facebook_feedback) {
            String url = "https://facebook.com/taara";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        if (view.getId() == R.id.twitter_feedback) {
            String url = "https://twitter.com/taara";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        if (view.getId() == R.id.youtube_feedback) {
            String url = "https://youtube.com/taara";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        if (view.getId() == R.id.whatsapp_feedback) {
            Uri mUri = Uri.parse("smsto:" + "+254 746 649576");
            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
            mIntent.setPackage("com.whatsapp");
            mIntent.putExtra("chat", true);


            if (mIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mIntent);
            } else {
                mIntent.setPackage("com.gbwhatsapp");
                if (mIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mIntent);
                } else {
                    Snackbar.make(mEdtEmail.getRootView(), "You dont have a suitable app to perform the action", Snackbar.LENGTH_SHORT).show();
                }

            }

        }


    }
}
