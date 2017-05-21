package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

/**
 * The landing page activity which opens after a user logs in.
 * It hosts the fragments which allow a use to view, add, and
 * modify items in the inventory.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class LandingPageActivity extends AppCompatActivity implements InventoryFragment.OnListFragmentInteractionListener,
        InventoryItemDetailsFragment.OnFragmentInteractionListener, InventoryAddFragment.InventoryAddListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryAddFragment inventoryAddFragment = new InventoryAddFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, inventoryAddFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#2196F3")));

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            InventoryFragment inventoryFragment = new InventoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, inventoryFragment)
                    .commit();
        }

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
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

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
