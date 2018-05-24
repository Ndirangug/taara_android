/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taara.android.taara;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.taara.android.taara.barcodereader.BarcodeCaptureActivity;
import com.taara.android.taara.custom_objects.RestApiCall;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class Cart extends Activity implements View.OnClickListener {


    static String[] retrievedResult;
    TextView storeId;
    private String statusMessage;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private String barcodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        storeId = findViewById(R.id.storeId);
        storeId.setText("2");
        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage = getResources().getString(R.string.barcode_success);
                    barcodeValue = barcode.displayValue;
                    Log.i(TAG, "Barcode read: " + statusMessage + ", " + barcodeValue);
                    retrieveProductOcuurence(barcodeValue, storeId.getText().toString());

                    Thread delay = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(2000);
                                String[] item = retrievedResult;
                                Log.i(TAG + ":extract", retrievedResult[0] + " " + retrievedResult[2] + " " + retrievedResult[5]);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    delay.start();


                } else {
                    statusMessage = getResources().getString(R.string.barcode_failure);
                    Log.i(TAG, "No barcode captured, intent data is null " + statusMessage);
                }
            } else {
                statusMessage = (String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void retrieveProductOcuurence(String qrCode, String storeId) {
        final Boolean[] complete = {false};
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.43.82/taaraBackend/?android_api_call=retrieveProductOccurrence&rfid=" + qrCode + "&storeID=" + storeId;
        //TODO:book item
        final RestApiCall restApiCall = new RestApiCall(getApplicationContext(), url);
        queue.add(restApiCall.makeJsonArrayRequest());
        RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
            @Override
            public void onRequestFinished(Request request) {
                retrievedResult = restApiCall.getResponseArray();
                Log.i(TAG + "oncom", retrievedResult[0] + retrievedResult[5]);
                complete[0] = true;
            }
        };

        queue.addRequestFinishedListener(requestFinishedListener);
        if (!complete[0]) {

        }

    }
}
