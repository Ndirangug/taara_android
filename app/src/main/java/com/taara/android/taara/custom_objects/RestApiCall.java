package com.taara.android.taara.custom_objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class RestApiCall {

    final static String[] responseArray = new String[10];
    final static String[][] responseArrayMultiDimensional = new String[35][7];
    Context context;
    String url;

    public RestApiCall(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public JsonArrayRequest makeJsonArrayRequest() {

        Log.i("Barcode retrieve", "Json request instantiated");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Barcode retrieve", "Inside request method");
                Log.i("response", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    Log.i("MULTIDIMEN", "Attempting read index " + String.valueOf(i));
                    try {
                        if (response.get(i).getClass().equals(JSONArray.class)) {
                            Log.i("MULTIDIMEN", "Attempting read index " + String.valueOf(i) + " of json array");
                            for (int j = 0; j < response.getJSONArray(i).length(); j++) {
                                responseArrayMultiDimensional[i][j] = response.getJSONArray(i).getString(j);
                                Log.i("MULTIDIMEN", response.getJSONArray(i).getString(j));
                            }


                        } else {
                            responseArray[i] = response.getString(i);
                            Log.i("response array", responseArray[i]);
                            Log.i("Barcode retrieve", responseArray[i]);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("response", error.toString());
                        Log.i("Barcode error", error.toString());
                    }
                });
        return jsonArrayRequest;
    }

    public String[] getResponseArray() {
        Log.i("API response ", responseArray[0] + responseArray[1]);
        Log.i("Barcode ", responseArray[0] + responseArray[1]);
        return responseArray;
    }

    public String[][] getResponseArrayMultiDimensional() {
        Log.i("MULTIDIMEN", "getting multidemsinal array ");
        return responseArrayMultiDimensional;
    }
}
