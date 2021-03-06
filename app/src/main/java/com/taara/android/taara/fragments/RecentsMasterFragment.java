package com.taara.android.taara.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.taara.android.taara.R;
import com.taara.android.taara.adapters.MyRecentsMasterRecyclerViewAdapter;
import com.taara.android.taara.custom_objects.ProductOccurrences;
import com.taara.android.taara.custom_objects.ProductOccurrences.ProductOccurrence;
import com.taara.android.taara.custom_objects.RestApiCall;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRecentsMasterFragmentInteractionListener}
 * interface.
 */
public class RecentsMasterFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    ProductOccurrences productOccurrences;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnRecentsMasterFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentsMasterFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecentsMasterFragment newInstance(int columnCount) {
        RecentsMasterFragment fragment = new RecentsMasterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        fragment.productOccurrences = new ProductOccurrences();
        fragment.connectivityManager = (ConnectivityManager) fragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        fragment.networkInfo = fragment.connectivityManager.getActiveNetworkInfo();
        fragment.productOccurrences = new ProductOccurrences();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recentsmaster_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setHasFixedSize(true);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //recyclerView.setAdapter(new MyRecentsMasterRecyclerViewAdapter(ProductOccurrences.ITEMS, mListener));

            // if (RecentActivity.IS_CONNECTED) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = getResources().getString(R.string.host) + "/taaraBackend/?android_api_call=allhistory&userID=" + mListener.getUserId();
            final RestApiCall restApiCall = new RestApiCall(getContext(), url);
            queue.add(restApiCall.makeJsonArrayRequest());

            RequestQueue.RequestFinishedListener requestFinishedListener = new RequestQueue.RequestFinishedListener() {
                @Override
                public void onRequestFinished(Request request) {
                    productOccurrences = new ProductOccurrences();
                    String[][] response = restApiCall.getResponseArrayMultiDimensional();
                    for (int i = 0; i < response.length; i++) {
                        productOccurrences.addItem(new ProductOccurrence(response[i][1], response[i][5], response[i][6], response[i][0], response[i][4], response[i][3], response[i][2]));
                    }

                    recyclerView.setAdapter(new MyRecentsMasterRecyclerViewAdapter(productOccurrences.ITEMS, mListener));
                    Log.i("Multidimen", productOccurrences.ITEMS.toString());
                }
            };
            queue.addRequestFinishedListener(requestFinishedListener);
//        } else {
//            //Snackbar.make(getActivity().findViewById(R.id.offersWebView).getRootView(), "Could not get recent activity.Check your connection", Snackbar.LENGTH_INDEFINITE ).show();
//            Toast.makeText(getContext(), "Could not get recent activity.Check your connection", Toast.LENGTH_LONG).show();
//        }
        }


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecentsMasterFragmentInteractionListener) {
            mListener = (OnRecentsMasterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecentsMasterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecentsMasterFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProductOccurrence item);
        String getUserId();
    }
}
