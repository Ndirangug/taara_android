package com.taara.android.taara.custom_objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class RestApiCall {

    final static String[] responseArray = new String[7];
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
                    try {
                        responseArray[i] = response.getString(i);
                        Log.i("response array", responseArray[i]);
                        Log.i("Barcode retrieve", responseArray[i]);
                    } catch (JSONException e) {
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
}
