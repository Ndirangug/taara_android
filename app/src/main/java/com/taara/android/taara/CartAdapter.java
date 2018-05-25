package com.taara.android.taara;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    /*index 0 => title
    index 1 => description
    index 2 => price*/

    static String[][] itemDetails = new String[40][40];

    public CartAdapter() {

    }

    public void addItem(String[] item) {
        itemDetails[getItemCount()] = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_card, parent, false);
        // wrap it in a ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.title.setText(itemDetails[i][0]);
        viewHolder.price.setText(itemDetails[i][2]);
        viewHolder.description.setText(itemDetails[i][1]);
    }

    @Override
    public int getItemCount() {
        int number = 0;
        for (int i = 0; i < itemDetails.length; i++) {
            try {
                if (!itemDetails[i][0].equals(null)) {
                    number++;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        }

        return number;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView price;
        TextView description;

        public ViewHolder(CardView card) {
            super(card);
            cardView = card;
            title = card.findViewById(R.id.item_title);
            price = card.findViewById(R.id.txtUnitPrice);
            description = card.findViewById(R.id.descriptionItem);
        }
    }
}
