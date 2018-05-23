package com.taara.android.taara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.taara.android.taara.custom_objects.User;

public class SignUp extends AppCompatActivity {

    EditText mFirstName, mSecondName, mPhone, mEmail, mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName = findViewById(R.id.edtFirstNameSignUp);
        mSecondName = findViewById(R.id.edtSecondNameSignUp);
        mEmail = findViewById(R.id.edtEmailSignUp);
        mPhone = findViewById(R.id.edtPhoneSignUp);
        mPassword = findViewById(R.id.editPasswordSignUp);
    }

    public void signUp(View view) {
        //handler for 'SIGN UP' button
        User user = new User(getApplicationContext());
        user.signUpEmailPassword(mFirstName.getText().toString(), mSecondName.getText().toString(), mPhone.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
        //TODO:PROGRESS ANIMATION

    }

    public void logInInstead(View view) {
        //handler for 'log in instead' option on log in screen
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
    }
}
