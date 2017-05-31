package ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Creates a new Inventory Item.
 *
 * @author Jasmine Dacones, Kaitlyn Kinerk
 * @version 1.0
 */
public class InventoryItem implements Serializable {

    /** ID of item */
    private String mId;
    /** Name of item */
    private String mItemName;
    /** Quantity of item */
    private String mQuantity;
    /** Price of item */
    private String mPrice;
    /** Expiration date of item */
    private String mExpiration;
    /** Expiration color-coding of item */
    private int mColor;


    /** String names for JSON parser */
    public static final String ITEM_ID = "id", ITEM_NAME = "name", QUANTITY = "quantity",
            PRICE = "price", EXPIRATION = "expirationdate";

    /**
     * Constructor to create a new inventory item
     * @param id id of inventory item
     * @param itemName name of inventory item
     * @param quantity quantity of inventory item
     * @param price price of inventory item
     * @param expiration expiration of inventory item
     */
    public InventoryItem(String id, String itemName, String quantity, String price, String expiration) {

        this.mId = id;

        if (itemName.length() != 0) {
            this.mItemName = itemName;
        } else {
            throw new IllegalArgumentException("Invalid item name");
        }

        if (isValidQuantity(quantity)) {
            this.mQuantity = quantity;
        } else {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (price.length() != 0) {
            this.mPrice = price;
        } else {
            throw new IllegalArgumentException("Invalid price");
        }

        if (expiration.length() != 0) {
            this.mExpiration = expiration;
        } else {
            throw new IllegalArgumentException("Invalid price");
        }



    }

    /**
     * Returns ID of inventory item.
     * @return ID of inventory item.
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets ID of inventory item.
     * @param id ID of inventory item.
     */
    public void setId(String id) {
        mId = id;
    }

    /**
     * Returns name of inventory item.
     * @return name of inventory item.
     */
    public String getItemName() {
        return mItemName;
    }

    /**
     * Sets name of inventory item.
     * @param mItemName name of inventory item.
     */
    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    /**
     * Returns quantity of inventory item.
     * @return quantity of inventory item.
     */
    public String getQuantity() {
        return mQuantity;
    }

    /**
     * Sets quantity of inventory item.
     * @param mQuantity quantity of inventory item.
     */
    public void setQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    /**
     * Returns price of inventory item.
     * @return price of inventory item.
     */
    public String getPrice() {
        return mPrice;
    }

    /**
     * Returns price of inventory item.
     * @param mPrice price of inventory item.
     */
    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    /**
     * Returns expiration date of inventory item.
     * @return expiration date of inventory item.
     */
    public String getExpiration() {
        return mExpiration;
    }

    /**
     * Returns expirationDate of inventory item.
     * @param mExpiration expiration date of inventory item.
     */
    public void setExpiration(String mExpiration) {
        this.mExpiration = mExpiration;
    }


    public boolean isValidQuantity(String quantity) {
        if (Integer.parseInt(quantity) > 0 && quantity.length() != 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Returns color of list row depending on item's expiration date.
     * @return color of list row depending on item's expiration date.
     */
    public int getColor() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date itemDate;
        Date compareDate;
        int color = 0;

        // Today's date
        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = todaysDateFormat.format(todaysDate);


        try {
            itemDate = df.parse(mExpiration);
            compareDate = df.parse(today);

            long diff = Math.round((itemDate.getTime() - compareDate.getTime()) / (double) 86400000);


            int difference = (int) diff;

            if (difference <= 0) {
                color = Color.RED;
            } else if (difference > 0 && difference <= 3) {
                color = Color.YELLOW;
            }

        } catch(ParseException e) {
            e.printStackTrace();
        }

        return color;
    }



    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param courseJSON
     * @return reason or null if successful.
     */
    public static String parseCourseJSON(String courseJSON, List<InventoryItem> courseList) {
        String reason = null;
        if (courseJSON != null) {
            try {
                JSONArray arr = new JSONArray(courseJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    InventoryItem item = new InventoryItem(obj.getString(InventoryItem.ITEM_ID),
                            obj.getString(InventoryItem.ITEM_NAME), obj.getString(InventoryItem.QUANTITY)
                            , obj.getString(InventoryItem.PRICE), obj.getString(InventoryItem.EXPIRATION));
                    courseList.add(item);
                }
            } catch (JSONException e) {
                reason =  "Use the round button to add some items!";
            }

        }
        return reason;
    }
}
