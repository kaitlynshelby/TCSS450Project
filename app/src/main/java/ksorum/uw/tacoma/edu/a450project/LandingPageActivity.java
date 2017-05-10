package ksorum.uw.tacoma.edu.a450project;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ksorum.uw.tacoma.edu.a450project.inventoryitem.InventoryItem;

public class LandingPageActivity extends AppCompatActivity implements InventoryFragment.OnListFragmentInteractionListener {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    }
}
