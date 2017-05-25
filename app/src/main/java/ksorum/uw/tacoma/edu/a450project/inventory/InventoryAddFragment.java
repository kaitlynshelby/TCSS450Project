package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import ksorum.uw.tacoma.edu.a450project.R;


/**
 * A fragment of the screen to add an item to the inventory.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class InventoryAddFragment extends Fragment {

    private InventoryAddListener mListener;

    /** Name of new item */
    private EditText mItemNameEditText;
    /** Quantity of new item */
    private EditText mItemQuantityEditText;
    /** Price of new item */
    private EditText mItemPriceEditText;
    /** Expiration of new item */
    private EditText mItemExpirationEditText;

    private SharedPreferences mSharedPreferences;

    /** URL of the location to add an item to the inventory */
    private final static String INVENTORY_ITEM_ADD_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/addInventoryItem.php?";


    public InventoryAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     *
     * @return A new instance of fragment InventoryAddFragment.
     */
    public static InventoryAddFragment newInstance() {
        InventoryAddFragment fragment = new InventoryAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory_add, container, false);

        mItemNameEditText = (EditText) v.findViewById(R.id.add_item_name);
        mItemQuantityEditText = (EditText) v.findViewById(R.id.add_item_quantity);
        mItemPriceEditText = (EditText) v.findViewById(R.id.add_item_price);
        mItemExpirationEditText = (EditText) v.findViewById(R.id.add_item_expiration);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button addCourseButton = (Button) v.findViewById(R.id.add_item_button);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addItem(url);
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InventoryAddListener) {
            mListener = (InventoryAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InventoryAddListener");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * Interface for adding an item to the inventory.
     */
    public interface InventoryAddListener {
        public void addItem(String url);
    }

    /**
     * Reads the user input of an item and adds to a database.
     * @param v the view of the fragment
     *
     * @return the string of the URL
     */
    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(INVENTORY_ITEM_ADD_URL);

        try {

            mSharedPreferences = getActivity().getApplicationContext().
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String itemName = mItemNameEditText.getText().toString();
            sb.append("&name=");
            sb.append(URLEncoder.encode(itemName, "UTF-8"));


            String itemQuantity = mItemQuantityEditText.getText().toString();
            sb.append("&quantity=");
            sb.append(URLEncoder.encode(itemQuantity, "UTF-8"));


            String itemPrice = mItemPriceEditText.getText().toString();
            sb.append("&price=");
            sb.append(URLEncoder.encode(itemPrice, "UTF-8"));

            String itemExpiration = mItemExpirationEditText.getText().toString();
            sb.append("&expirationdate=");
            sb.append(URLEncoder.encode(itemExpiration, "UTF-8"));

            String user = mSharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            Log.i("InventoryAddFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

}
