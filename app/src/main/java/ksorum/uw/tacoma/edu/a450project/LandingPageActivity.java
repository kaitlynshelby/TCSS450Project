package ksorum.uw.tacoma.edu.a450project;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ksorum.uw.tacoma.edu.a450project.inventoryitem.InventoryItem;

public class LandingPageActivity extends AppCompatActivity implements InventoryFragment.OnListFragmentInteractionListener, InventoryItemDetailsFragment.OnFragmentInteractionListener, InventoryAddFragment.InventoryAddListener {

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

    @Override
    public void addItem(String url) {

    }
}
