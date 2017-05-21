package ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListFragment;

/**
 * Created by Jasmine D on 5/9/2017.
 */

public class ShoppingListItem implements Serializable {

    private String mName;
    private String mQuantity;
    private String mPrice;


    public static final String ITEM_NAME = "name", QUANTITY = "quantity",
            PRICE = "price";

    public ShoppingListItem(String name, String quantity, String price) {
        this.mName = name;
        this.mQuantity = quantity;
        this.mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mItemName) {
        this.mName = mItemName;
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


    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param shoppingListJSON
     * @return reason or null if successful.
     */
    public static String parseListJSON(String shoppingListJSON, List<ShoppingListItem> shoppingList) {
        String reason = null;
        if (shoppingListJSON != null) {
            try {
                JSONArray arr = new JSONArray(shoppingListJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    ShoppingListItem item = new ShoppingListItem(obj.getString(ShoppingListItem.ITEM_NAME), obj.getString(ShoppingListItem.QUANTITY)
                            , obj.getString(ShoppingListItem.PRICE));
                    shoppingList.add(item);
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

}
