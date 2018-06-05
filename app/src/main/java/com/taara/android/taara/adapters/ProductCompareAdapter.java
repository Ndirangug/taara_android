package com.taara.android.taara.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taara.android.taara.R;
import com.taara.android.taara.fragments.RecentsMasterFragment;
import com.taara.android.taara.recents.RecentProductOccurrences;

import java.util.List;

public class ProductCompareAdapter extends RecyclerView.Adapter<ProductCompareAdapter.ViewHolder> {


    private final List<RecentProductOccurrences.ProductOccurrence> mValues;
    private final RecentsMasterFragment.OnListFragmentInteractionListener mListener;
    private final double mPriceToCompareAgainst;
    private final Context mContext;

    public ProductCompareAdapter(List<RecentProductOccurrences.ProductOccurrence> items, RecentsMasterFragment.OnListFragmentInteractionListener listener, double priceToCompareAgainst, Context context) {
        mValues = items;
        mListener = listener;
        mPriceToCompareAgainst = priceToCompareAgainst;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recentsmaster, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        double price = Double.valueOf(mValues.get(i).price);
        double estimatedSaings;
        if (mPriceToCompareAgainst > price){
            estimatedSaings = mPriceToCompareAgainst - price;
        }
        else {
            estimatedSaings = 0;
        }
        viewHolder.storeName.setText(mValues.get(i).store_name);
        viewHolder.price.setText(mValues.get(i).price);
        viewHolder.price.setText(mContext.getResources().getString(R.string.estimated_savings) + String.valueOf(estimatedSaings));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView storeName;
        public final TextView price;
        public final TextView estimated_savings;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            storeName = view.findViewById(R.id.store_name_compare);
            price = view.findViewById(R.id.price_compare);
            estimated_savings = view.findViewById(R.id.estimated_saving);


        }
    }
}
