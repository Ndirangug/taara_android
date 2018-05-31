package com.taara.android.taara.custom_objects;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taara.android.taara.LogIn;
import com.taara.android.taara.R;

import static com.android.volley.VolleyLog.TAG;

public class User {
   /* This Manages log in and sign up for users
        -Firebase Login
        -Firebase sign up
        -Read and write remote database table 'users'
        -Write user details  to shared prefreences */

   String firstName;
   String secondName;
   String phoneNumber;
   String email;
    Context context;
    Boolean logIn;
    Boolean signUp;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    String userId;


    public User(Context context) {
        this.context = context;

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

    }



    public Boolean getLogIn() {
        return logIn;
    }

    public int logIn(String phoneOrEmail, final String password) {
        RequestQueue queue;
        logIn = false;
        final int[] returnValue = {0};
        //declare firebase variables
       final FirebaseAuth mAuth = FirebaseAuth.getInstance();
       final FirebaseUser[] currentUser = new FirebaseUser[1];

       //check if variable 'phoneOrEmail' is an email address
       final CharSequence EMAIL_IDENTIFIER  = "@";
        final RestApiCall restApiCall;
       if (phoneOrEmail.contains(EMAIL_IDENTIFIER)){
           this.email = phoneOrEmail;
           //api call to retrieve user details with email

           final String[] responseArray = new String[6];
           queue = Volley.newRequestQueue(this.context);
           String url = context.getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=retrieveUserWithEmail&email=" + this.email;
           restApiCall = new RestApiCall(context, url);
           queue.add(restApiCall.makeJsonArrayRequest());


       } else {
           //'phooeOrEmail' did not have an '@' and was therefore a phone number
           this.phoneNumber = phoneOrEmail;
           //api call to retrieve user details with phone and use retrieved email to sign in
           final String[] responseArray = new String[7];
           queue = Volley.newRequestQueue(this.context);
           String url = context.getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=retrieveUserWithPhone&phone=" + this.phoneNumber;

           restApiCall = new RestApiCall(context, url);
           if (!(networkInfo == null)) {
               queue.add(restApiCall.makeJsonArrayRequest());
           } else {
               Toast.makeText(context, "Check your connection and try again", Toast.LENGTH_LONG).show();
           }



       }

        RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
            @Override
            public void onRequestFinished(Request request) {
                String[] apiCallResult = restApiCall.getResponseArray();
                Log.i("API", apiCallResult[0] + apiCallResult[1]);
                if (apiCallResult[0].equals("success")) {
                    //call was successfull proceed with details retrieval
                    firstName = apiCallResult[1];
                    secondName = apiCallResult[2];
                    email = apiCallResult[3];
                    phoneNumber = apiCallResult[4];
                    userId = apiCallResult[5];

                    Log.i("success", firstName + secondName + phoneNumber + "successfully retrieved");

                    //check if user is signed already
                    //if not sign in, else, just retrieve details

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("login", "sign in with email success for user " + email);
                                        currentUser[0] = mAuth.getCurrentUser();
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
                                        sharedPreferences.edit().putString("FIRST_NAME", firstName)
                                                .putString("SECOND_NAME", secondName)
                                                .putString("EMAIL", email)
                                                .putString("PHONE", phoneNumber)
                                                .putString("USER_ID", userId)
                                                .commit();
                                        returnValue[0] = 1;
                                        logIn = true;
                                        Log.i("return", String.valueOf(returnValue[0]));
                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(context, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    Toast.makeText(context, "Email or phone number doesn't exist. Enter correct details or sign up", Toast.LENGTH_LONG).show();
                }
            }
        };
        if (!(networkInfo == null)) {
            queue.addRequestFinishedListener(requestFinishedListener);
        } else {
            Toast.makeText(context, "Check your connection and try again", Toast.LENGTH_LONG).show();
        }




        return returnValue[0];
    }

    public boolean signUpEmailPassword(final String firstName, final String secondName, final String phoneNumber, final String email, final String password) {
        final boolean[] returnValue = {false};
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            returnValue[0] = true;
                            //firebase signUp successful
                            Log.i(TAG, "firebase sign up successful");
                            //write firstName, secondName, Phone, Email to remote db
                            final RequestQueue queue = Volley.newRequestQueue(context);
                            String url = context.getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=createNewUser&firstName=" + firstName + "&secondName=" + secondName + "&phone=" + phoneNumber + "&email=" + email;

                            RestApiCall restApiCall = new RestApiCall(context, url);
                            queue.add(restApiCall.makeJsonArrayRequest());

                            RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
                                @Override
                                public void onRequestFinished(Request request) {

                                    Intent logIn = new Intent(context, LogIn.class);
                                    logIn.putExtra("EMAIL", email);
                                    logIn.putExtra("PASSWORD", password);
                                    logIn.putExtra("LOGIN", true);
                                    logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(logIn);
                                    Toast.makeText(context, "SIGN UP SUCCESSFUL", Toast.LENGTH_LONG);
                                }
                            };
                            queue.addRequestFinishedListener(requestFinishedListener);
                            //executeLogIn


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Sign Up Failed" + task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        return returnValue[0];
   }

}
