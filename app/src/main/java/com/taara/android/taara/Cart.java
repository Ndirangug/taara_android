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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.taara.android.taara.adapters.CartAdapter;
import com.taara.android.taara.barcodereader.BarcodeCaptureActivity;
import com.taara.android.taara.custom_objects.RestApiCall;


/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class Cart extends Activity implements View.OnClickListener {

    private static final double SERVICE_CHARGE_RATE = 0.005;
    static double amtSubtotal = 0;
    static double amtServiceCharge = 0;
    static double amtTotal = 0;
    RecyclerView cartRecycler;
    CartAdapter cartAdapter;
    TextView txtsubtotal;
    TextView txtserviceCharge;
    TextView txtTotal;
    Button btnCheckout;
    private static final int REMOVE_ITEM_FROM_CART = 9002;

    public static String[][] cartItems;
    static String[] retrievedResult;
    //static String[] bookedResult;
    String storeName;
    String storeTillNo;
    String storeId;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private String barcodeValue;
    double toSubtract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        SharedPreferences sharedPreferences = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        storeId = sharedPreferences.getString("STORE_ID", "0");
        storeName = sharedPreferences.getString("STORE_NAME", "0");
        storeTillNo = sharedPreferences.getString("MPESA_TILL_NO", "0");
        Log.i("STORE", "id =" + storeId);
        if (storeId.equals("0")) {
            Snackbar.make(cartRecycler, "CHECK IN WAS UNSUCCESSFUL.GO BACK AND TRY AGAIN", Snackbar.LENGTH_SHORT).show();

        }
        txtserviceCharge = findViewById(R.id.amountServiceCharge);
        txtsubtotal = findViewById(R.id.amountSubTotal);
        txtTotal = findViewById(R.id.amountTotal);
        btnCheckout = findViewById(R.id.cartCheckout);
        txtsubtotal.setText("0.00");
        txtserviceCharge.setText("0.00");
        txtTotal.setText("0.00");
        cartAdapter = new CartAdapter(getApplicationContext());
        cartRecycler = findViewById(R.id.cartRecycler);
        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cartRecycler.setAdapter(cartAdapter);

        Snackbar.make(cartRecycler.getRootView(), "Successfully checked in to " + storeName, Snackbar.LENGTH_LONG).show();

        findViewById(R.id.read_barcode).setOnClickListener(this);

        ImageView btnBack = findViewById(R.id.cartBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        if (v.getId() == R.id.removeItem) {
            //capture rfid from barcode to remove it from cart
            //int positon = cartRecycler.getChildAdapterPosition(v);
            Log.i(TAG + "return", "value: " + barcodeValue);
            CardView cardView = (CardView) v.getParent().getParent();
            CartAdapter.ViewHolder viewHolder = (CartAdapter.ViewHolder) cartRecycler.getChildViewHolder(cardView);
            TextView price = viewHolder.price;
            toSubtract = Double.parseDouble(price.getText().toString());
            if (cartRecycler.getChildCount() > 1) {
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, REMOVE_ITEM_FROM_CART);
            } else {
                toSubtract = 0;
                Snackbar.make(cartRecycler, "Cart cannot be empty.Add another item to remove this one", Snackbar.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.cartCheckout) {
            cartItems = CartAdapter.itemDetails;
            Intent intent = new Intent(getApplicationContext(), CheckoutSummary.class);
            String[][] cartItems = cartAdapter.getItemDetails();
            intent.putExtra("cart_items", cartItems);
            intent.putExtra("STORE_ID", storeId);
            startActivity(intent);
        }

        if (v.getId() == R.id.cartBack) {
            onBackPressed();
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
     *
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String statusMessage;
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage = getResources().getString(R.string.barcode_success);
                    barcodeValue = barcode.displayValue;
                    Log.i(TAG, "Barcode read: " + statusMessage + ", " + barcodeValue);
                    retrieveProductOcuurence(barcodeValue, storeId);
                    Log.i(TAG + "retrieve", "attempetd retrieve");

                    Thread delay = new Thread() {

                        @Override
                        public void run() {

                            try {
                                sleep(3500);
                                String[] item = retrievedResult;
                                Log.i(TAG + ":extract", retrievedResult[0] + " " + retrievedResult[2] + " " + retrievedResult[5]);
                                String[] itemDetails = {retrievedResult[5], retrievedResult[6], retrievedResult[2], retrievedResult[3], retrievedResult[4]};
                                amtSubtotal += Integer.parseInt(retrievedResult[2]);
                                amtServiceCharge = Math.round((SERVICE_CHARGE_RATE * amtSubtotal) * 100.0) / 100.0;
                                amtTotal = (amtSubtotal + amtServiceCharge);
                                cartAdapter.addItem(itemDetails);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cartRecycler.setAdapter(cartAdapter);
                                        txtsubtotal.setText(String.valueOf(amtSubtotal));
                                        txtserviceCharge.setText(String.valueOf(amtServiceCharge));
                                        txtTotal.setText(String.valueOf(amtTotal));

                                    }
                                });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                Snackbar.make(txtserviceCharge.getRootView(), "Error:321 Item details retrieval failed.", Snackbar.LENGTH_LONG).show();
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Snackbar.make(txtserviceCharge.getRootView(), "Error:343 Item details retrieval failed.", Snackbar.LENGTH_LONG).show();
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
            if (requestCode == REMOVE_ITEM_FROM_CART) {
                if (resultCode == CommonStatusCodes.SUCCESS) {

                    if (data != null) {
                        try {
                            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                            statusMessage = getResources().getString(R.string.barcode_success);
                            barcodeValue = barcode.displayValue;
                            Log.i(TAG + "remove", "Barcode read: " + statusMessage + ", " + barcodeValue);
                            cartAdapter.removeFromCart(barcodeValue);
                            Log.i(TAG + "sub", "Preparing to remove");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG + "sub", "Now removing");
                                    amtSubtotal -= toSubtract;
                                    amtServiceCharge = Math.round((SERVICE_CHARGE_RATE * amtSubtotal) * 100.0) / 100.0;
                                    amtTotal = (amtSubtotal + amtServiceCharge);
                                    txtsubtotal.setText(String.valueOf(amtSubtotal));
                                    txtserviceCharge.setText(String.valueOf(amtServiceCharge));
                                    txtTotal.setText(String.valueOf(amtTotal));
                                    cartRecycler.setAdapter(cartAdapter);


                                }
                            });
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            Snackbar.make(btnCheckout.getRootView(), "Not returned vale " + e.toString(), Snackbar.LENGTH_INDEFINITE).show();
                        }
                    } else {
                        statusMessage = getResources().getString(R.string.barcode_failure);
                        Log.i(TAG, "No barcode captured, intent data is null " + statusMessage);
                    }

                } else {
                    statusMessage = (String.format(getString(R.string.barcode_error),
                            CommonStatusCodes.getStatusCodeString(resultCode)));
                    Log.i(TAG, "Result not success " + statusMessage);
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void retrieveProductOcuurence(String qrCode, String storeId) {
        Log.i(TAG + "retrieve", "started");
        final Boolean[] complete = {false};
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=retrieveProductOccurrence&rfid=" + qrCode + "&storeID=" + storeId;
        //String urlBook = "http://192.168.43.82/taaraBackend/?android_api_call=markItemBooked&rfid=" + qrCode + "&storeID=" + storeId;
        final RestApiCall restApiCall = new RestApiCall(getApplicationContext(), url);
        //final RestApiCall restApiCall1Book = new RestApiCall(getApplicationContext(), urlBook);
        queue.add(restApiCall.makeJsonArrayRequest());
        //queue.add(restApiCall1Book.makeJsonArrayRequest());
        Log.i(TAG + "retrieve", "listener starting");
        RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
            @Override
            public void onRequestFinished(Request request) {
                retrievedResult = restApiCall.getResponseArray();
                //bookedResult = restApiCall1Book.getResponseArray();
                Log.i(TAG + "oncom", retrievedResult[0] + retrievedResult[5]);
                //Log.i(TAG+"booked:", "result"+bookedResult[0]);
                complete[0] = true;
            }
        };

        queue.addRequestFinishedListener(requestFinishedListener);
        if (!complete[0]) {

        }

    }


}
