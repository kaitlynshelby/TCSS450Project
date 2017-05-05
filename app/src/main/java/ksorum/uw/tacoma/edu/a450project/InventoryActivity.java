package ksorum.uw.tacoma.edu.a450project;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class InventoryActivity extends Activity {

    private ArrayList<String> addedItems;
    private ArrayAdapter<String> arrayAdapter;
    private EditText editText;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Button button = (Button)findViewById(R.id.add_item_button);

        editText = (EditText)findViewById(R.id.add_item_field);

        addedItems = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(arrayAdapter);
    }

    public void buttonClick(View view) {
        String newItem = editText.getText().toString();
        addedItems.add(newItem);
        arrayAdapter.add(newItem);
        editText.setText("");

        arrayAdapter.notifyDataSetChanged();
    }
}
