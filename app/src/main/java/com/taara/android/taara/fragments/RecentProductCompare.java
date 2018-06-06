package com.taara.android.taara.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taara.android.taara.R;
import com.taara.android.taara.custom_objects.ProductOccurrences;
import com.taara.android.taara.custom_objects.RestApiCall;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentProductCompareInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentProductCompare#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentProductCompare extends Fragment implements RecentsMasterFragment.OnRecentsMasterFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "PRICE_TO_COMPARE";
    private static final String ARG_PARAM2 = "BARCODE_TO_COMPARE";

    private View view;
    private Toolbar mToolbar;
    //private RecyclerView mRecyclerViewCompare;
    public static OnFragmentProductCompareInteractionListener mListener;
    private static Bundle mArgs;
    // TODO: Rename and change types of parameters
    private double mPriceToCompare;
    private String mBarcodeToCompare;
    private ProductOccurrences mProductOccurrences;


    public RecentProductCompare() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param priceToCompare Parameter 1.
     * @param barcodeToCompare Parameter 2.
     * @return A new instance of fragment RecentProductCompare.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentProductCompare newInstance(double priceToCompare, String barcodeToCompare) {
        RecentProductCompare fragment = new RecentProductCompare();
        mArgs = new Bundle();
        mArgs.putDouble(ARG_PARAM1, priceToCompare);
        mArgs.putString(ARG_PARAM2, barcodeToCompare);
        fragment.setArguments(mArgs);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPriceToCompare = getArguments().getDouble(ARG_PARAM1);
            mBarcodeToCompare = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_product_compare, container, false);
        //mToolbar = view.findViewById(R.id.recentsCompareToolBar);
        //mRecyclerViewCompare = view.findViewById(R.id.fromOtherSupermarketsCompare);
        //mRecyclerViewCompare.setHasFixedSize(true);
        //mRecyclerViewCompare.setLayoutManager(new LinearLayoutManager(getContext()));
        final ListView listView = view.findViewById(R.id.fromOtherSupermarketsCompare);


        Log.i("compare", "instantiated recycler view");

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=compare&barcode=" + mBarcodeToCompare;
        Log.i("compare", "url passed " + url);
        final RestApiCall restApiCall = new RestApiCall(getContext(), url);
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
                listView.setAdapter(new com.taara.android.taara.adapters.ListAdapter(getContext(), R.layout.other_supermarkets_compare, getLayoutInflater(), mProductOccurrences.ITEMS));
                //mRecyclerViewCompare.setAdapter(new ProductCompareAdapter(mProductOccurrences.ITEMS,  mListener,  getContext() ));
                Log.i("compare", mProductOccurrences.ITEMS.toString());
            }
        };
        Log.i("compare", "before finished listener added");
        queue.addRequestFinishedListener(requestFinishedListener);
        Log.i("compare", "after finished listener added");

        Log.i("compare", "returning view");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.recent_product_compare, menu);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentProductCompareInteractionListener) {
            mListener = (OnFragmentProductCompareInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentProductCompareInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListFragmentInteraction(ProductOccurrences.ProductOccurrence item) {
        newInstance(Double.valueOf(item.price), item.barcode);
    }

    @Override
    public String getUserId() {
        return null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentProductCompareInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
