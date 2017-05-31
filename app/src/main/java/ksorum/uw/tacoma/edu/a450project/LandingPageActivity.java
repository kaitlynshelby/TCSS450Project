package ksorum.uw.tacoma.edu.a450project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ksorum.uw.tacoma.edu.a450project.home.HomeActivity;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryAddFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryEditFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryItemDetailsFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.MyInventoryRecyclerViewAdapter;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.MyShoppingListRecyclerViewAdapter;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingItemDetailsFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListAddFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListEditFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * The landing page activity which opens after a user logs in.
 * It hosts the fragments which allow a use to view, add, delete, and
 * modify items in the inventory and shopping list.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class LandingPageActivity extends AppCompatActivity implements InventoryFragment.OnListFragmentInteractionListener,
        InventoryItemDetailsFragment.OnFragmentInteractionListener, InventoryAddFragment.InventoryAddListener,
        MyInventoryRecyclerViewAdapter.OnDeleteItem, ShoppingListFragment.OnShoppingListFragmentInteractionListener,
        InventoryEditFragment.OnInventoryEditInteractionListener, MyShoppingListRecyclerViewAdapter.OnDeleteItem,
        ShoppingListAddFragment.ShoppingListAddListener, ShoppingItemDetailsFragment.ShoppingDetailsFragmentInteractionListener,
        ShoppingListEditFragment.OnShoppingListEditInteractionListener {


    /**
     * The base url for the webservices launched in this class
     */
    private static final String URL =
            "http://cssgate.insttech.washington.edu/~ksorum/";

    /**
     * If an item was deleted from the database (needed to have class-level scope to access outside
     * of inner classes.
     */
    private boolean mDelete;

    /**
     * The custom adapter to use with the TabLayout
     */
    private CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;

    /**
     * The TabLayout used for this activity.
     */
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up viewpager and adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_landing);
        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), LandingPageActivity.this);
        viewPager.setAdapter(mCustomFragmentPagerAdapter);

        setTitle("What's in My Fridge?");


        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs_landing);
        mTabLayout.setupWithViewPager(viewPager);

        // launch correct fragment based on selected tab
        if ((savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null)
                && mTabLayout.getSelectedTabPosition() == 0) {
            InventoryFragment inventoryFragment = new InventoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_landing, inventoryFragment)
                    .commit();
        } else if ((savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.shop_list) == null)
                && mTabLayout.getSelectedTabPosition() == 1) {
            ShoppingListFragment shopFragment = new ShoppingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_landing, shopFragment)
                    .commit();
        }

        // launch correct fragments when tabs are switched
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    InventoryFragment inventoryFragment = new InventoryFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_landing, inventoryFragment)
                            .commit();

                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    ShoppingListFragment shopFragment = new ShoppingListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_landing, shopFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // launch correct Add fragment based on currently selected tab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTabLayout.getSelectedTabPosition() == 0) {
                    InventoryAddFragment inventoryAddFragment = new InventoryAddFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_landing, inventoryAddFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    ShoppingListAddFragment shoppingAddFragment = new ShoppingListAddFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_landing, shoppingAddFragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

    }


    /**
     * Launches the item details fragment for the item
     * that is clicked on.
     *
     * @param item the inventory item that is clicked on.
     */
    @Override
    public void onListFragmentInteraction(InventoryItem item) {
        InventoryItemDetailsFragment detailsFragment = new InventoryItemDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(InventoryItemDetailsFragment.INVENTORY_ITEM_SELECTED, item);
        detailsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_landing, detailsFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Launches correct menu for each tab (can't share shopping list from inventory tab)
     *
     * @param menu the activity menu
     * @return the options menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTabLayout.getSelectedTabPosition() == 1) {
            getMenuInflater().inflate(R.menu.shop_options_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.inventory_options_menu, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Adds an item to the database.
     *
     * @param url url to add to the database.
     */
    @Override
    public void addItem(String url) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AddItemTask task = new AddItemTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this,
                    "Cannot add item without a network connection.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean deleteInventoryItem(String itemid, String itemName, final String quan, String itemPrice) {
        mDelete = false;
        final String itemId = itemid;
        final String name = itemName;
        final String quantity = quan;
        final String price = itemPrice;

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = "You are about to delete " + name + ". Would you like to add this item to your Shopping List?";
            builder.setMessage(message)
                    // add item to shopping list after delete from inventory
                    .setPositiveButton("Yes, add to Shopping List", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String deleteURL = buildDeleteURL(itemId);
                            DeleteItemTask task1 = new DeleteItemTask();
                            task1.execute(new String[]{deleteURL});

                            String addURL = buildAddURL(name, price, quantity);
                            AddItemTask task2 = new AddItemTask();
                            task2.execute(new String[]{addURL});
                            mDelete = true;
                            mTabLayout.getTabAt(1).select();

                        }
                    })
                    // just delete item
                    .setNegativeButton("No, just delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String url = buildDeleteURL(itemId);
                            DeleteItemTask task = new DeleteItemTask();
                            task.execute(new String[]{url});
                            mDelete = true;
                            InventoryFragment fragment = new InventoryFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_landing, fragment)
                                    .commit();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(this,
                    "Cannot delete item without a network connection.",
                    Toast.LENGTH_SHORT).show();
        }
        return mDelete;

    }

    @Override
    public boolean deleteShopItem(String itemid, String itemName, final String quan, String itemPrice) {
        mDelete = false;
        final String itemId = itemid;
        final String name = itemName;
        final String quantity = quan;
        final String price = itemPrice;

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = "You are about to delete " + name + ". Would you like to add this item to your Inventory?";
            builder.setMessage(message)
                    // add shopping item to inventory after delete
                    .setPositiveButton("Yes, add to Inventory", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String deleteURL = buildShopDeleteURL(itemId);
                            DeleteItemTask task1 = new DeleteItemTask();
                            task1.execute(new String[]{deleteURL});

                            String addURL = buildShopAddURL(name, price, quantity);
                            AddItemTask task2 = new AddItemTask();
                            task2.execute(new String[]{addURL});
                            mDelete = true;
                            mTabLayout.getTabAt(0).select();
                            ;
                        }
                    })
                    // just delete
                    .setNegativeButton("No, just delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String url = buildShopDeleteURL(itemId);
                            DeleteItemTask task = new DeleteItemTask();
                            task.execute(new String[]{url});
                            mDelete = true;
                            ShoppingListFragment fragment = new ShoppingListFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_landing, fragment)
                                    .commit();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(this,
                    "Cannot delete item without a network connection.",
                    Toast.LENGTH_SHORT).show();
        }

        return mDelete;
    }

    /**
     * Builds the url to add a shopping list item
     *
     * @param name     item name
     * @param price    item price
     * @param quantity item quantity
     * @return the webservice url
     */
    private String buildShopAddURL(String name, String price, String quantity) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("addInventoryItem.php?");

            sb.append("name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            sb.append("&price=");
            sb.append(URLEncoder.encode(price, "UTF-8"));

            sb.append("&quantity=");
            sb.append(URLEncoder.encode(quantity, "UTF-8"));

            String user = sharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        } catch (Exception e) {

        }
        return sb.toString();
    }

    /**
     * Builds url for delete shopping list item webservice
     *
     * @param id the id of the item to delete
     * @return the webservice url
     */
    private String buildShopDeleteURL(String id) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("deleteShoppingItem.php?");

            sb.append("id=");
            sb.append(URLEncoder.encode(id, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        } catch (Exception e) {

        }
        return sb.toString();
    }

    /**
     * Builds url for delete inventory item webservice
     *
     * @param id the id of the item to delete
     * @return the delete webservice url
     */
    private String buildDeleteURL(String id) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("deleteInventoryItem.php?");

            sb.append("id=");
            sb.append(URLEncoder.encode(id, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        } catch (Exception e) {

        }
        return sb.toString();
    }

    /**
     * Builds url for add inventory item webservice
     *
     * @param name     item name
     * @param price    item price
     * @param quantity item quantity
     * @return the url for the add inventory item webservice
     */
    private String buildAddURL(String name, String price, String quantity) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("addShoppingItem.php?");

            sb.append("name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            sb.append("&price=");
            sb.append(URLEncoder.encode(price, "UTF-8"));

            sb.append("&quantity=");
            sb.append(URLEncoder.encode(quantity, "UTF-8"));

            String user = sharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        } catch (Exception e) {

        }
        return sb.toString();
    }

    @Override
    public void onShoppingListFragmentInteraction(ShoppingListItem item) {
        ShoppingItemDetailsFragment detailsFragment = new ShoppingItemDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ShoppingItemDetailsFragment.SHOPPING_ITEM_SELECTED, item);
        detailsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_landing, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void editInventoryItem(String url) {
    }

    /**
     * Adds an item to the database.
     *
     * @param url url to add to the database.
     */
    @Override
    public void addShoppingItem(String url) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            AddItemTask task = new AddItemTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this,
                    "Cannot add item without a network connection.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShoppingDetailsInteraction(Uri uri) {

    }

    @Override
    public void editShoppingItem(String url) {

    }


    /**
     * Launches AsyncTask to execute the web service to add an item
     * to the inventory.
     */
    public class DeleteItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    java.net.URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to delete course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Item deleted from list"
                            , Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete item. Check your connection and try again."
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {

            }
        }
    }

    /**
     * Launches AsyncTask to execute the web service to add an item
     * to the inventory.
     */
    private class AddItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Item added to list"
                            , Toast.LENGTH_SHORT)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add item. Please enter valid data."
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add item. Please enter valid data."
                        , Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
