package com.taara.android.taara.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taara.android.taara.R;
import com.taara.android.taara.custom_objects.ProductOccurrences;
import com.taara.android.taara.fragments.RecentProductCompare;
import com.taara.android.taara.fragments.RecentsMasterFragment;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> implements RecentsMasterFragment.OnRecentsMasterFragmentInteractionListener {


    private static double mPriceToCompareAgainst;
    LayoutInflater layoutInflater;
    private List<ProductOccurrences.ProductOccurrence> mValues;
    private RecentProductCompare.OnFragmentProductCompareInteractionListener mComparelistener;
    private Context mContext;


    public ListAdapter(@NonNull Context context, int resource, LayoutInflater inflater, List<ProductOccurrences.ProductOccurrence> items) {
        super(context, resource);
        mContext = context;
        layoutInflater = inflater;
        mValues = items;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;


        convertView = layoutInflater.inflate(R.layout.other_supermarkets_compare, null, false);
        holder = new ViewHolder(convertView);
        convertView.setTag("holder");

        double price = Double.valueOf(mValues.get(position).price);
        double estimatedSaings;
        if (mPriceToCompareAgainst > price) {
            estimatedSaings = mPriceToCompareAgainst - price;
        } else {
            estimatedSaings = 0;
        }
        holder.storeName.setText(mValues.get(position).store_name);
        holder.price.setText(mValues.get(position).price);
        holder.estimated_savings.setText(mContext.getResources().getString(R.string.estimated_savings) + String.valueOf(estimatedSaings));

        return convertView;
    }

    @Override
    public void onListFragmentInteraction(ProductOccurrences.ProductOccurrence item) {

    }

    @Override
    public String getUserId() {
        return null;
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
