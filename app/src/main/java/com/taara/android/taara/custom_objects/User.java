package com.taara.android.taara.custom_objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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


    public User(Context context) {
        this.context = context;
    }

   public boolean logIn(String phoneOrEmail, final String password){

        boolean returnValue = false;
        //declare firebase variables
       final FirebaseAuth mAuth = FirebaseAuth.getInstance();
       final FirebaseUser[] currentUser = new FirebaseUser[1];

       //check if variable 'phoneOrEmail' is an email address
       final CharSequence EMAIL_IDENTIFIER  = "@";
       RestApiCall restApiCall;
       if (phoneOrEmail.contains(EMAIL_IDENTIFIER)){
           this.email = phoneOrEmail;
           //api call to retrieve user details with email

           final String[] responseArray = new String[6];
           RequestQueue queue = Volley.newRequestQueue(this.context);
           String url = "http://192.168.43.82/taaraBackend/?android_api_call=retrieveUserWithEmail&email=" + this.email;
           restApiCall = new RestApiCall(context, url);
           queue.add(restApiCall.makeJsonArrayRequest());


       } else {
           //'phooeOrEmail' did not have an '@' and was therefore a phone number
           this.phoneNumber = phoneOrEmail;
           //api call to retrieve user details with phone and use retrieved email to sign in
           final String[] responseArray = new String[6];
           RequestQueue queue = Volley.newRequestQueue(this.context);
           String url = "http://192.168.43.82/taaraBackend/?android_api_call=retrieveUserWithPhone&phone=" + this.phoneNumber;

           restApiCall = new RestApiCall(context, url);
           queue.add(restApiCall.makeJsonArrayRequest());


       }

       String[] apiCallResult = restApiCall.getResponseArray();

       if (apiCallResult[0] == "success") {
           //call was successfull proceed with details retrieval
           this.firstName = apiCallResult[1];
           this.secondName = apiCallResult[2];
           this.email = apiCallResult[3];
           this.phoneNumber = apiCallResult[4];

           Log.i("success", this.firstName + this.secondName + this.phoneNumber + "successfully retrieved");

           //check if user is signed already
           //if not sign in, else, just retrieve details
           if (mAuth.getCurrentUser() == null) {
               mAuth.signInWithEmailAndPassword(this.email, password)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   Log.i("login", "sign in with email success for user " + email);
                                   currentUser[0] = mAuth.getCurrentUser();
                               } else {
                                   Log.w(TAG, "signInWithEmail:failure", task.getException());
                                   Toast.makeText(context, "Authentication failed.",
                                           Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

           }
       } else {
           Toast.makeText(context, "Email or phone number doesn't exist. Enter correct details or sign up", Toast.LENGTH_LONG).show();
       }


        return returnValue;
   }
}
