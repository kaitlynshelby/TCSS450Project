package ksorum.uw.tacoma.edu.a450project;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    private SectionsAdapter mSectionsAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSectionsAdapter = new SectionsAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsAdapter adapter = new SectionsAdapter(getSupportFragmentManager());
        adapter.addFragment(new InventoryFragment(), "Inventory");
        adapter.addFragment(new ShoppingListFragment(), "Shopping List");
        viewPager.setAdapter(adapter);

    }
}

