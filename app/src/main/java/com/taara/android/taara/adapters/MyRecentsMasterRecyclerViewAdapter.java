package com.taara.android.taara.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taara.android.taara.R;
import com.taara.android.taara.fragments.RecentsMasterFragment.OnListFragmentInteractionListener;
import com.taara.android.taara.recents.RecentProductOccurrences.ProductOccurrence;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProductOccurrence} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRecentsMasterRecyclerViewAdapter extends RecyclerView.Adapter<MyRecentsMasterRecyclerViewAdapter.ViewHolder> {

    private final List<ProductOccurrence> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRecentsMasterRecyclerViewAdapter(List<ProductOccurrence> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recentsmaster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mProductTitle.setText(mValues.get(position).title);
        holder.mProductDescription.setText(mValues.get(position).description);
        holder.mDateBought.setText(mValues.get(position).date_bought);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mProductTitle;
        public final TextView mProductDescription;
        public final TextView mDateBought;
        public ProductOccurrence mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProductTitle = (TextView) view.findViewById(R.id.product_title);
            mProductDescription = (TextView) view.findViewById(R.id.product_description);
            mDateBought = view.findViewById(R.id.date_bought);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductDescription.getText() + "'";
        }
    }
}
