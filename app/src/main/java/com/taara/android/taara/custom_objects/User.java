package com.taara.android.taara.custom_objects;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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

   public boolean logIn(String phoneOrEmail, final String password){

        boolean returnValue = false;
        //declare firebase variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser;

       //check if variable 'phoneOrEmail' is an email address
       final CharSequence EMAIL_IDENTIFIER  = "@";
       if (phoneOrEmail.contains(EMAIL_IDENTIFIER)){
           this.email = phoneOrEmail;

                   //api call to retrieve user details with email

       } else {
           //'phooeOrEmail' did not have an '@' and was therefore a phone number
           this.phoneNumber = phoneOrEmail;
           //api call to retrieve user details with phone and use retrieved email to sign in



       }

       if ((this.email != null) || (this.phoneNumber != null)){
           returnValue = true;
       }
        //check if user is signed already
        //if not sign in, else, just retrieve details
        if (mAuth.getCurrentUser() == null){




        }

        return returnValue;
   }
}
