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
 * Created by Kaitlyn on 5/29/2017.
 */

public class InventoryItemsDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "InventoryItems.db";
    private static final String INVENTORY_ITEMS_TABLE = "InventoryItems";

    private InventoryItemsDBHelper mInventoryItemsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public InventoryItemsDB(Context context) {
        mInventoryItemsDBHelper = new InventoryItemsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mInventoryItemsDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param id
     * @param name
     * @param quantity
     * @param price
     * @param expirationdate
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

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    /**
    * Returns the list of items from the local InventoryItems table.
    * @return list
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
        for (int i=0; i<c.getCount(); i++) {
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




    class InventoryItemsDBHelper extends SQLiteOpenHelper {

        private final String CREATE_INVENTORY_ITEMS_SQL;

        private final String DROP_INVENTORY_ITEMS_SQL;

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