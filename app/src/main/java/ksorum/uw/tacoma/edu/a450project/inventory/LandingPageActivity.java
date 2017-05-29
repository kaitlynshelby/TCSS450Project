package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import ksorum.uw.tacoma.edu.a450project.CustomFragmentPagerAdapter;
import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.MyShoppingListRecyclerViewAdapter;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingItemDetailsFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListAddFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListEditFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * The landing page activity which opens after a user logs in.
 * It hosts the fragments which allow a use to view, add, and
 * modify items in the inventory.
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


    private static final String URL =
            "http://cssgate.insttech.washington.edu/~ksorum/";

    private boolean mDelete;

    private CustomFragmentPagerAdapter adapter;

    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager_landing);
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), LandingPageActivity.this);
        viewPager.setAdapter(adapter);

        setTitle("What's in My Fridge?");

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs_landing);
        mTabLayout.setupWithViewPager(viewPager);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            InventoryFragment inventoryFragment = new InventoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_landing, inventoryFragment)
                    .commit();
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    InventoryFragment inventoryFragment = new InventoryFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_landing, inventoryFragment)
                            .commit();

                } else {
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
                } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }



    /**
     * Adds an item to the database.
     * @param url url to add to the database.
     */
    @Override
    public void addItem(String url) {
        AddItemTask task = new AddItemTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    public boolean deleteInventoryItem(String itemName, final String quan, String itemPrice) {
        mDelete = false;
        final String name = itemName;
        final String quantity = quan;
        final String price = itemPrice;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "You are about to delete " + name + ". Would you like to add this item to your Shopping List?";
        builder.setMessage(message)
                .setPositiveButton("Yes, add to Shopping List", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String deleteURL = buildDeleteURL(name);
                        DeleteItemTask task1 = new DeleteItemTask();
                        task1.execute(new String[]{deleteURL});

                        String addURL = buildAddURL(name, price, quantity);
                        AddItemTask task2 = new AddItemTask();
                        task2.execute(new String[]{addURL});
                        mDelete = true;
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No, just delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = buildDeleteURL(name);
                        DeleteItemTask task = new DeleteItemTask();
                        task.execute(new String[]{url});
                        mDelete = true;
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return mDelete;
    }

    @Override
    public boolean deleteShopItem(String itemName, final String quan, String itemPrice) {
        mDelete = false;
        final String name = itemName;
        final String quantity = quan;
        final String price = itemPrice;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "You are about to delete " + name + ". Would you like to add this item to your Inventory?";
        builder.setMessage(message)
                .setPositiveButton("Yes, add to Inventory", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String deleteURL = buildShopDeleteURL(name);
                        DeleteItemTask task1 = new DeleteItemTask();
                        task1.execute(new String[]{deleteURL});

                        String addURL = buildShopAddURL(name, price, quantity);
                        AddItemTask task2 = new AddItemTask();
                        task2.execute(new String[]{addURL});
                        mDelete = true;
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No, just delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = buildShopDeleteURL(name);
                        DeleteItemTask task = new DeleteItemTask();
                        task.execute(new String[]{url});
                        mDelete = true;
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return mDelete;
    }

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

        }
        catch(Exception e) {

        }
        return sb.toString();
    }

    private String buildShopDeleteURL(String name) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("deleteShoppingItem.php?");

            sb.append("name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            String user = sharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        }
        catch(Exception e) {

        }
        return sb.toString();
    }

    private String buildDeleteURL(String name) {
        StringBuilder sb = new StringBuilder(URL);

        try {

            SharedPreferences sharedPreferences = getSharedPreferences
                    (getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            sb.append("deleteInventoryItem.php?");

            sb.append("name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            String user = sharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingListActivity", sb.toString());

        }
        catch(Exception e) {

        }
        return sb.toString();
    }

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

        }
        catch(Exception e) {

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
     * @param url url to add to the database.
     */
    @Override
    public void addShoppingItem(String url) {
        AddItemTask task = new AddItemTask();
        task.execute(new String[]{url.toString()});
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
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete item. Check your connection and try again."
                            , Toast.LENGTH_LONG)
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
                            , Toast.LENGTH_LONG)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    Toast.makeText(getApplicationContext(), "Item failed to add. Check your connection and try again."
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {

            }
        }
    }
}
