package ksorum.uw.tacoma.edu.a450project;

/*
https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout
 */
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryItemDetailsFragment;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

public class TestTabActivity extends AppCompatActivity
        implements InventoryFragment.OnListFragmentInteractionListener,
        InventoryItemDetailsFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    private CustomFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tab);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), TestTabActivity.this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);

        return true;
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
                .replace(R.id.fragment_container_2, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
