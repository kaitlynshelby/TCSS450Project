package ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem;

import android.graphics.Color;
import android.util.Log;

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
 * Created by Jasmine D on 5/9/2017.
 */

public class InventoryItem implements Serializable {

    private String mItemName;
    private String mQuantity;
    private String mPrice;
    private String mExpiration;
    private int mColor;


    public static final String ITEM_NAME = "name", QUANTITY = "quantity",
            PRICE = "price", EXPIRATION = "expirationdate";

    public InventoryItem(String itemName, String quantity, String price, String expiration) {
        this.mItemName = itemName;
        this.mQuantity = quantity;
        this.mPrice = price;
        this.mExpiration = expiration;

    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getExpiration() {
        return mExpiration;
    }

    public void setExpiration(String mExpiration) {
        this.mExpiration = mExpiration;
    }


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

            if (difference <= 1) {
                color = Color.RED;
            } else if (difference >= 2 && difference <= 3) {
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
                    InventoryItem item = new InventoryItem(obj.getString(InventoryItem.ITEM_NAME), obj.getString(InventoryItem.QUANTITY)
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
