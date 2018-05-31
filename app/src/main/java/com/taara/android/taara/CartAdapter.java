package com.taara.android.taara;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    static int offset = 0;
    static String[][] itemDetails = new String[40][5];
    /*index 0 => title
    index 1 => description
    index 2 => price
    index 3 => rfid
    index 4 => VAT*/
    private Context mContext;

    CartAdapter(Context context) {
        mContext = context;
    }

    public String[][] getItemDetails() {
        return itemDetails;
    }

    public void addItem(String[] item) {
        itemDetails[getItemCount()] = item;
    }


    public void removeFromCart(String rfid) {
        for (int i = 0; i < itemDetails.length; i++) {
            try {


                if (itemDetails[i][3].equals(rfid)) {
                    while (!itemDetails[i + 1][0].equals(null) && i <= getItemCount()) {
                        itemDetails[i][0] = itemDetails[i + 1][0];
                        itemDetails[i + 1][0] = null;
                        itemDetails[i][1] = itemDetails[i + 1][1];
                        itemDetails[i + 1][1] = null;
                        itemDetails[i][2] = itemDetails[i + 1][2];
                        itemDetails[i + 1][2] = null;
                        itemDetails[i][3] = itemDetails[i + 1][3];
                        itemDetails[i + 1][3] = null;

                        i++;
                    }

                    break;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();

            }
        }

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
        int empty = 0;
        for (int i = 0; i < itemDetails.length; i++) {
            try {
                if (!itemDetails[i][0].equals(null)) {
                    number++;
                } else {
                    empty += 1;
                    if (empty > 3) {
                        break;
                    }
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
