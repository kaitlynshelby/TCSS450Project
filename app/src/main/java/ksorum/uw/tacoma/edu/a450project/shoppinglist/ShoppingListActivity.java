package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryAddFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.LandingPageActivity;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

public class ShoppingListActivity extends AppCompatActivity implements
        ShoppingListFragment.OnShoppingListFragmentInteractionListener,
        ShoppingListAddFragment.ShoppingListAddListener,
        ShoppingItemDetailsFragment.ShoppingDetailsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

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
                    Toast.makeText(getApplicationContext(), "Item successfully added!"
                            , Toast.LENGTH_LONG)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
