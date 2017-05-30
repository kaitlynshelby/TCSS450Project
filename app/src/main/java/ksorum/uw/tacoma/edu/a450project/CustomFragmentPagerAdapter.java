package ksorum.uw.tacoma.edu.a450project;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListFragment;

/**
 * Adapter for the TabLayout used in the LandingPageActivity.
 *
 * @Author Jasmine Dacones
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Inventory", "Shopping List" };
    private Context context;

    /**
     * Constructor for a CustomFragmentPagerAdapter
     * @param fm fragment manager for TabLayout
     * @param context context of the application
     */
    public CustomFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InventoryFragment();
            case 1:
                return new ShoppingListFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}