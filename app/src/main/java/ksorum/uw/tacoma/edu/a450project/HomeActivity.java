package ksorum.uw.tacoma.edu.a450project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        TabHost host = (TabHost)findViewById((R.id.tabHost));
        host.setup();

        // Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Inventory");
        spec.setContent(R.id.inventory_tab);
        spec.setIndicator("Inventory");
        host.addTab(spec);

        // Tab 2
        spec = host.newTabSpec("Shopping List");
        spec.setContent(R.id.shoppinglist_tab);
        spec.setIndicator("Shopping List");
        host.addTab(spec);
    }
}
