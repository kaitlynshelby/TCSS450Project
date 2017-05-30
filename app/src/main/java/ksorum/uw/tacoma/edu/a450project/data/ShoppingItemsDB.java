package ksorum.uw.tacoma.edu.a450project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * Created by Kaitlyn on 5/29/2017.
 */

public class ShoppingItemsDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ShoppingItems.db";
    private static final String SHOPPING_ITEMS_TABLE = "ShoppingItems";

    private ShoppingItemsDBHelper mShoppingItemsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public ShoppingItemsDB(Context context) {
        mShoppingItemsDBHelper = new ShoppingItemsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mShoppingItemsDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param id
     * @param name
     * @param quantity
     * @param price
     * @return true or false
     */
    public boolean insertShoppingItem(String id, String
            name, String quantity, String price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);

        long rowId = mSQLiteDatabase.insert("ShoppingItems", null, contentValues);
        return rowId != -1;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    /**
     * Returns the list of items from the local ShoppingItems table.
     * @return list
     */
    public List<ShoppingListItem> getItems() {

        String[] columns = {
                "id", "name", "quantity", "price"
        };

        Cursor c = mSQLiteDatabase.query(
                SHOPPING_ITEMS_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<ShoppingListItem> list = new ArrayList<ShoppingListItem>();
        for (int i=0; i<c.getCount(); i++) {
            String id = c.getString(0);
            String name = c.getString(1);
            String quantity = c.getString(2);
            String price = c.getString(3);
            ShoppingListItem item = new ShoppingListItem(id, name, quantity, price);
            list.add(item);
            c.moveToNext();
        }

        return list;
    }

    /**
     * Delete all the data from the SHOPPING_ITEMS_TABLE
     */
    public void deleteItems() {
        mSQLiteDatabase.delete(SHOPPING_ITEMS_TABLE, null, null);
    }




    class ShoppingItemsDBHelper extends SQLiteOpenHelper {

        private final String CREATE_SHOPPING_ITEMS_SQL;

        private final String DROP_SHOPPING_ITEMS_SQL;

        public ShoppingItemsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_SHOPPING_ITEMS_SQL = context.getString(R.string.CREATE_SHOPPING_ITEMS_SQL);
            DROP_SHOPPING_ITEMS_SQL = context.getString(R.string.DROP_SHOPPING_ITEMS_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_SHOPPING_ITEMS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_SHOPPING_ITEMS_SQL);
            onCreate(sqLiteDatabase);

        }
    }
}