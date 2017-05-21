package ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jasmine D on 5/9/2017.
 */

public class InventoryItem implements Serializable {

    private String mItemName;
    private String mQuantity;
    private String mPrice;
    private String mExpiration;


    public static final String ITEM_NAME = "itemName", QUANTITY = "quantity",
            PRICE = "price", EXPIRATION = "expiration";

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
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

}
