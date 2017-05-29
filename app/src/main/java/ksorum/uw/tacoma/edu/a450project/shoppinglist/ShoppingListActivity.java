package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListEditFragment;

import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

public class ShoppingListActivity extends AppCompatActivity implements
        ShoppingListFragment.OnShoppingListFragmentInteractionListener,
        ShoppingListAddFragment.ShoppingListAddListener,
        ShoppingItemDetailsFragment.ShoppingDetailsFragmentInteractionListener,
        MyShoppingListRecyclerViewAdapter.OnDeleteItem,
        ShoppingListEditFragment.OnShoppingListEditInteractionListener {

    private static final String URL =
            "http://cssgate.insttech.washington.edu/~ksorum/";

    private boolean mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.shop_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingListAddFragment shoppingAddFragment = new ShoppingListAddFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.shopping_list_container, shoppingAddFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.shop_list) == null) {
            ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.shopping_list_container, shoppingListFragment)
                    .commit();
        }

    }

    @Override
    public void onShoppingListFragmentInteraction(ShoppingListItem item) {
        ShoppingItemDetailsFragment detailsFragment = new ShoppingItemDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ShoppingItemDetailsFragment.SHOPPING_ITEM_SELECTED, item);
        detailsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.shopping_list_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shop_options_menu, menu);
        return true;
    }


    /**
     * Adds an item to the database.
     * @param url url to add to the database.
     */
    @Override
    public void addShoppingItem(String url) {
        AddShopItemTask task = new AddShopItemTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    public void onShoppingDetailsInteraction(Uri uri) {

    }

    @Override
    public boolean deleteItem(String itemName, final String quan, String itemPrice) {
       mDelete = false;
       final String name = itemName;
       final String quantity = quan;
       final String price = itemPrice;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "You are about to delete " + name + ". Would you like to add this item to your Inventory?";
        builder.setMessage(message)
                .setPositiveButton("Yes, add to Inventory", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String deleteURL = buildDeleteURL(name);
                        DeleteItemTask task1 = new DeleteItemTask();
                        task1.execute(new String[]{deleteURL});

                        String addURL = buildAddURL(name, price, quantity);
                        AddShopItemTask task2 = new AddShopItemTask();
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

    private String buildDeleteURL(String name) {
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

    private String buildAddURL(String name, String price, String quantity) {
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
                    Toast.makeText(getApplicationContext(), "Failed to delete item. Check your connection and try again. "
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
    private class AddShopItemTask extends AsyncTask<String, Void, String> {

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
                    Toast.makeText(getApplicationContext(), "Failed to add item. Check your connection and try again."
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
            }
        }
    }


}
