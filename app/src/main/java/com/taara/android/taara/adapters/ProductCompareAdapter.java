package com.taara.android.taara.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taara.android.taara.R;
import com.taara.android.taara.custom_objects.ProductOccurrences;
import com.taara.android.taara.fragments.RecentProductCompare;
import com.taara.android.taara.fragments.RecentsMasterFragment;

import java.util.List;

public class ProductCompareAdapter extends RecyclerView.Adapter<ProductCompareAdapter.ViewHolder> implements RecentsMasterFragment.OnRecentsMasterFragmentInteractionListener {


    public static double mPriceToCompareAgainst;
    private final List<ProductOccurrences.ProductOccurrence> mValues;
    private final RecentProductCompare.OnFragmentProductCompareInteractionListener mComparelistener;
    private final Context mContext;

    public ProductCompareAdapter(List<ProductOccurrences.ProductOccurrence> items, RecentProductCompare.OnFragmentProductCompareInteractionListener compareListener, Context context) {
        mValues = items;
        mComparelistener = compareListener;
        mContext = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_supermarkets_compare, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String estimatedSaving;
        viewHolder.storeName.setText(mValues.get(i).store_name);
        viewHolder.price.setText(mValues.get(i).price);
        String strprice = viewHolder.price.getText().toString();
        Log.i("compare", strprice);

        try {
            double dblPrice = Double.parseDouble(strprice);
            if (mPriceToCompareAgainst > dblPrice) {
                Log.i("dlll", String.valueOf(dblPrice));
                estimatedSaving = " Savings: Kshs" + String.valueOf(mPriceToCompareAgainst - dblPrice);

            } else {
                Log.i("compare1", String.valueOf(mPriceToCompareAgainst));
                Log.i("compare2", String.valueOf(dblPrice));
                estimatedSaving = " Savings: Kshs" + "0";
            }
            Log.i("estimate", strprice);

            viewHolder.estimated_savings.setText(String.valueOf(estimatedSaving));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(mContext, strprice, Toast.LENGTH_LONG).show();
        }

        viewHolder.price.setText("Price: Kshs  " + mValues.get(i).price);


    }

    @Override
    public int getItemCount() {
        int count = 0;
        int nullOnes = 0;

        for (int i = 0; i < mValues.size(); i++) {
            if (!(null == mValues.get(i).price)) {
                count += 1;
            } else {
                nullOnes += 1;
            }

            if (nullOnes > 3) {
                break;
            }
        }
        Log.i("count items", String.valueOf(count));
        return count;
    }

    @Override
    public void onListFragmentInteraction(ProductOccurrences.ProductOccurrence item) {
        mPriceToCompareAgainst = Double.valueOf(item.price);
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
