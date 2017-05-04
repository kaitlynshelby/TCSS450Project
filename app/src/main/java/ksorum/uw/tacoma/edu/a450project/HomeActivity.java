package ksorum.uw.tacoma.edu.a450project;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
SignUpFragment.OnFragmentInteractionListener,
MainFragment.OnFragmentInteractionListener {

    private SectionsAdapter mSectionsAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .addToBackStack(null)
                .commit();


       /* mSectionsAdapter = new SectionsAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);

        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);*/

    }

    public boolean launchLogin(View v) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();

        return true;
    }


    public boolean launchSignUp(View v) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SignUpFragment())
                .addToBackStack(null)
                .commit();

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


 /*   private void setupViewPager(ViewPager viewPager) {
        SectionsAdapter adapter = new SectionsAdapter(getSupportFragmentManager());
        adapter.addFragment(new InventoryFragment(), "Inventory");
        adapter.addFragment(new ShoppingListFragment(), "Shopping List");
        viewPager.setAdapter(adapter);

    }*/
}

