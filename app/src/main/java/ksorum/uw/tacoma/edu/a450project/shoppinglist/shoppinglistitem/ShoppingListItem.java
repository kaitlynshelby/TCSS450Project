package ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListFragment;

/**
 * Creates a new Shopping List item.
 *
 * @author Kaitlyn Kinerk
 */
public class ShoppingListItem implements Serializable {

    /** ID of shopping list item */
    private String mId;
    /** Name of shopping list item */
    private String mName;
    /** Quantity of shopping list item */
    private String mQuantity;
    /** Price of shopping list item */
    private String mPrice;

    /** String names for JSON parser */
    public static final String ITEM_ID = "id", ITEM_NAME = "name", QUANTITY = "quantity",
            PRICE = "price";

    /**
     * Constructor to create a new Shopping List item.
     * @param id id of shopping list item
     * @param name name of shopping list item
     * @param quantity quantity of shopping list item
     * @param price price of shopping list item
     */
    public ShoppingListItem(String id, String name, String quantity, String price) {
        this.mId = id;
        this.mName = name;
        this.mQuantity = quantity;
        this.mPrice = price;
    }

    /**
     * Returns ID of shopping list item.
     * @return ID of shopping list item.
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets ID of shopping list item.
     * @param id ID of shopping list item.
     */
    public void setId(String id) {
        mId = id;
    }

    /**
     * Returns name of shopping list item.
     * @return name of shopping list item.
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets name of shopping list item.
     * @param mItemName name of shopping list item.
     */
    public void setName(String mItemName) {
        this.mName = mItemName;
    }

    /**
     * Returns quantity of shopping list item.
     * @return quantity of shopping list item.
     */
    public String getQuantity() {
        return mQuantity;
    }

    /**
     * Sets quantity of shopping list item.
     * @param mQuantity quantity of shopping list item.
     */
    public void setQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    /**
     * Returns price of shopping list item.
     * @return price of shopping list item.
     */
    public String getPrice() {
        return mPrice;
    }

    /**
     * Sets price of shopping list item.
     * @param mPrice price of shopping list item.
     */
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
                    ShoppingListItem item = new ShoppingListItem(obj.getString(ShoppingListItem.ITEM_ID),
                            obj.getString(ShoppingListItem.ITEM_NAME), obj.getString(ShoppingListItem.QUANTITY)
                            , obj.getString(ShoppingListItem.PRICE));
                    shoppingList.add(item);
                }
            } catch (JSONException e) {
                reason =  "Use the round button to add some items!";
            }

        }
        return reason;
    }

}
