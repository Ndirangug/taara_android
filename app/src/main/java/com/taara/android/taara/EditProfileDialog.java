package com.taara.android.taara;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taara.android.taara.custom_objects.RestApiCall;

public class EditProfileDialog extends DialogFragment {
    onProfileChangedListener profileChangedListener;

    EditText edtFirtsName, edtSecondName, edtEmail, edtPhone;
    String firstName, secondName, email, phone;
    SharedPreferences sharedPreferences;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_profle_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.save_changes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firstName = edtFirtsName.getText().toString();
                        secondName = edtSecondName.getText().toString();
                        email = edtEmail.getText().toString();
                        phone = edtPhone.getText().toString();

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=updateProfle&first_name=" + firstName + "&second_name=" + secondName + "&phone=" + phone + "&email=" + email;
                        final RestApiCall restApiCall = new RestApiCall(getContext(), url);
                        queue.add(restApiCall.makeJsonArrayRequest());

                        RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
                            @Override
                            public void onRequestFinished(Request request) {
                                String[] response = restApiCall.getResponseArray();
                                if (response[0].equals("Update Success")) {
                                    //Toast.makeText(getContext(), "Profile Info successfully upadated", Toast.LENGTH_SHORT).show();
                                    sharedPreferences.edit().putString("FIRST_NAME", firstName)
                                            .putString("SECOND_NAME", secondName)
                                            .putString("EMAIL", email)
                                            .putString("PHONE", phone)
                                            .commit();
                                    profileChangedListener.updateNavigationDrawerHeader(sharedPreferences);

                                    Log.i("profile", firstName + " " + secondName);
                                    Snackbar.make(edtEmail.getRootView(), "Profile successfully changed", Snackbar.LENGTH_LONG).show();

                                } else {
                                    Log.i("profile", firstName + " " + secondName + "failed");
                                    Snackbar.make(edtEmail.getRootView(), "Profile  change failed", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        };

                        queue.addRequestFinishedListener(requestFinishedListener);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setMessage(getResources().getString(R.string.upate_profile));
        sharedPreferences = getContext().getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        edtFirtsName = view.findViewById(R.id.edtFirstName);
        edtSecondName = view.findViewById(R.id.edtSecodName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtphone);
        edtEmail.setEnabled(false);
        edtFirtsName.setText(sharedPreferences.getString("FIRST_NAME", null));
        edtSecondName.setText(sharedPreferences.getString("SECOND_NAME", null));
        edtEmail.setText(sharedPreferences.getString("EMAIL", null));
        edtPhone.setText(sharedPreferences.getString("PHONE", null));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            profileChangedListener = (onProfileChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onProfileChangedListener");
        }
    }

    public interface onProfileChangedListener {
        public void updateNavigationDrawerHeader(SharedPreferences sharedPreferences);
    }
}
