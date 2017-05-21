package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

public class ShoppingListActivity extends AppCompatActivity implements
        ShoppingListFragment.OnShoppingListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.shopping_list_container, shoppingListFragment)
                    .commit();
        }

    }

    @Override
    public void onShoppingListFragmentInteraction(ShoppingListItem item) {

    }
}
