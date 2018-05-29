package com.taara.android.taara;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.taara.android.taara.barcodereader.BarcodeCaptureActivity;
import com.taara.android.taara.custom_objects.RestApiCall;


public class CheckInDialogFragment extends DialogFragment {

    static String TAG = "Barcode Dialog";
    String statusMessage;
    String barcodeValue;
    TextView storeName;
    ProgressBar progressBar;
    String scanResult = "Failed...try again";
    private int RC_BARCODE_CAPTURE = 9001;
    private String storeId;
    private String[] retrievedResult;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.check_in, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.check_in)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE).edit().putString("STORE_NAME", retrievedResult[1])
                                .putString("STORE_LOCATION", retrievedResult[2])
                                .putString("STORE_ID", retrievedResult[0])
                                .putString("MPESA_TILL_NO", retrievedResult[3])
                                .commit();
                        Log.i("TAG", "added to shared prefrencs " + retrievedResult[1]);
                        Intent intent = new Intent(getContext(), Cart.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        CheckInDialogFragment.super.onCancel(dialog);
                    }
                });
        // Create the AlertDialog object and return it
        storeName = view.findViewById(R.id.storeName);
        storeName.setVisibility(View.INVISIBLE);
        progressBar = view.findViewById(R.id.progressBar);

        Intent intent = new Intent(getContext(), BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);

        return builder.create();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage = getResources().getString(R.string.barcode_success);
                    barcodeValue = barcode.displayValue;
                    Log.i(TAG, "Barcode read: " + statusMessage + ", " + barcodeValue);
                    storeId = barcodeValue;

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=getStore&storeID=" + storeId;
                    final RestApiCall restApiCall = new RestApiCall(getContext(), url);
                    queue.add(restApiCall.makeJsonArrayRequest());
                    Log.i(TAG + "retrieve", "listener starting");
                    RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
                        @Override
                        public void onRequestFinished(Request request) {
                            retrievedResult = restApiCall.getResponseArray();
                            Log.i(TAG, "on complete " + retrievedResult[1]);
                            progressBar.setVisibility(View.INVISIBLE);
                            storeName.setVisibility(View.VISIBLE);
                            try {

                                if (!retrievedResult[1].equals(null)) {
                                    scanResult = retrievedResult[1].toString().toUpperCase();
                                }

                                storeName.setText(scanResult);
                                Log.i("STORE", retrievedResult[1]);
                            } catch (NullPointerException e) {
                                e.printStackTrace();

                            }


                        }
                    };

                    queue.addRequestFinishedListener(requestFinishedListener);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
