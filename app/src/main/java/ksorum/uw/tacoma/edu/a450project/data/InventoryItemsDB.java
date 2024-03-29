package ksorum.uw.tacoma.edu.a450project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

/**
 * Internal storage of inventory items
 *
 * @author Kaitlyn Kinerk
 */
public class InventoryItemsDB {

    /**
     * Database version number
     */
    public static final int DB_VERSION = 1;
    /**
     * Name of database
     */
    public static final String DB_NAME = "InventoryItems.db";
    /**
     * Name of inventory items database table
     */
    private static final String INVENTORY_ITEMS_TABLE = "InventoryItems";
    /**
     * Helper for the inventory items database
     */
    private InventoryItemsDBHelper mInventoryItemsDBHelper;
    /**
     * SQLite database
     */
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Constructor for the inventory items internal storage database.
     *
     * @param context context of the application
     */
    public InventoryItemsDB(Context context) {
        mInventoryItemsDBHelper = new InventoryItemsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mInventoryItemsDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     *
     * @param id             id of inventory item
     * @param name           name of inventory item
     * @param quantity       quantity of inventory item
     * @param price          price of inventory item
     * @param expirationdate expiration date of inventory item
     * @return true or false
     */
    public boolean insertInventoryItem(String id, String
            name, String quantity, String price, String expirationdate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        contentValues.put("expirationdate", expirationdate);

        long rowId = mSQLiteDatabase.insert("InventoryItems", null, contentValues);
        return rowId != -1;
    }

    /**
     * Closes the SQLite database.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }

    /**
     * Returns the list of items from the local InventoryItems table.
     *
     * @return list of items from the local InvenotryItems table.
     */
    public List<InventoryItem> getItems() {

        String[] columns = {
                "id", "name", "quantity", "price", "expirationdate"
        };

        Cursor c = mSQLiteDatabase.query(
                INVENTORY_ITEMS_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<InventoryItem> list = new ArrayList<InventoryItem>();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(0);
            String name = c.getString(1);
            String quantity = c.getString(2);
            String price = c.getString(3);
            String expirationdate = c.getString(4);
            InventoryItem item = new InventoryItem(id, name, quantity, price, expirationdate);
            list.add(item);
            c.moveToNext();
        }

        return list;
    }

    /**
     * Delete all the data from the INVENTORY_ITEMS_TABLE
     */
    public void deleteItems() {
        mSQLiteDatabase.delete(INVENTORY_ITEMS_TABLE, null, null);
    }


    /**
     * Helper class for the internal storage.
     */
    class InventoryItemsDBHelper extends SQLiteOpenHelper {

        /**
         * Creates Inventory Items table
         */
        private final String CREATE_INVENTORY_ITEMS_SQL;

        /**
         * Drops Inventory table in the database
         */
        private final String DROP_INVENTORY_ITEMS_SQL;

        /**
         * Constructor for a new InventoryItems helper
         *
         * @param context context of the application
         * @param name    name of the database
         * @param factory factory
         * @param version database version number
         */
        public InventoryItemsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_INVENTORY_ITEMS_SQL = context.getString(R.string.CREATE_INVENTORY_ITEMS_SQL);
            DROP_INVENTORY_ITEMS_SQL = context.getString(R.string.DROP_INVENTORY_ITEMS_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_INVENTORY_ITEMS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_INVENTORY_ITEMS_SQL);
            onCreate(sqLiteDatabase);

        }
    }
}