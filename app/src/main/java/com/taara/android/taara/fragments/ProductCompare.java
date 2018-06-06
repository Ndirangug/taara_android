package com.taara.android.taara.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taara.android.taara.R;
import com.taara.android.taara.adapters.ProductCompareAdapter;
import com.taara.android.taara.custom_objects.ProductOccurrences;
import com.taara.android.taara.custom_objects.RestApiCall;

public class ProductCompare extends AppCompatActivity {

    //ListView listView;

    public static String mTitle, mDescription, mStore, mPrice;
    ImageView compareback;
    private ProductOccurrences mProductOccurrences;
    private RecyclerView mRecyclerViewCompare;
    private TextView txtTitle, txtDescription, txtPrice, txtStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recent_product_compare);
        //listView = findViewById(R.id.fromOtherSupermarketsCompare);
        txtDescription = findViewById(R.id.comparereDesctiption);
        txtPrice = findViewById(R.id.comparePrice);
        txtStore = findViewById(R.id.compareStore);
        txtTitle = findViewById(R.id.compareTitle);

        txtTitle.setText(mTitle);
        txtDescription.setText(mDescription);
        txtStore.setText(mStore);
        txtPrice.setText(mPrice);

        mRecyclerViewCompare = findViewById(R.id.fromOtherSupermarketsCompare);
        mRecyclerViewCompare.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewCompare.setHasFixedSize(true);

        compareback = findViewById(R.id.compareBack);
        compareback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showComparison();
    }

    private void showComparison() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=compare&barcode=" + getIntent().getStringExtra("BARCODE");
        Log.i("compare", "url passed " + url);
        final RestApiCall restApiCall = new RestApiCall(getApplicationContext(), url);
        Log.i("compare", "rest api instantiated");
        queue.add(restApiCall.makeJsonArrayRequest());
        Log.i("compare", "request added to queue");
        RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
            @Override
            public void onRequestFinished(Request request) {
                Log.i("compare", "listener started...");
                mProductOccurrences = new ProductOccurrences();
                String[][] response = restApiCall.getResponseArrayMultiDimensional();

                Log.i("compare", "Array retrieved ....getting into product ocuurences list population loop");

                for (int i = 0; i < response.length; i++) {
                    Log.i("compare", "inside loop..iteration " + i);
                    mProductOccurrences.addItem(new ProductOccurrences.ProductOccurrence(response[i][1], response[i][2], response[i][3], response[i][4], "", response[i][5], response[i][6]));
                }

                Log.i("compare", "setting adpter for recycler view");
                //listView.setAdapter(new com.taara.android.taara.adapters.ListAdapter(getApplicationContext(), R.layout.other_supermarkets_compare, getLayoutInflater(), mProductOccurrences.ITEMS));
                mRecyclerViewCompare.setAdapter(new ProductCompareAdapter(mProductOccurrences.ITEMS, RecentProductCompare.mListener, getApplicationContext()));
                Log.i("compare", mProductOccurrences.ITEMS.toString());
            }
        };
        Log.i("compare", "before finished listener added");
        queue.addRequestFinishedListener(requestFinishedListener);
        Log.i("compare", "after finished listener added");

    }

}
