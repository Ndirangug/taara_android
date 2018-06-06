package com.taara.android.taara.custom_objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample title for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProductOccurrences {

    /**
     * An array of sample (dummy) items.
     */
    public  final List<ProductOccurrence> ITEMS = new ArrayList<ProductOccurrence>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ProductOccurrence> ITEM_MAP = new HashMap<String, ProductOccurrence>();



    public  void addItem(ProductOccurrence item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.barcode, item);
    }


    /**
     * A dummy item representing a product occurrence.
     */
    public static class ProductOccurrence {
        public final String barcode;
        public final String title;
        public final String description;
        public final String price;
        public final String date_bought;
        public final String store_name;
        public final String VAT;

        public ProductOccurrence(String barcode, String title, String description, String price, String date_bought, String store_name, String VAT) {
            this.barcode = barcode;
            this.title = title;
            this.description = description;
            this.price = price;
            this.date_bought = date_bought;
            this.store_name = store_name;
            this.VAT = VAT;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
