package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListAddFragment.ShoppingListAddListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListAddFragment extends Fragment {

    /** URL of the location to add an item to the inventory */
    private final static String SHOPPING_ITEM_ADD_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/addShoppingItem.php?";

    /** Name of new item */
    private EditText mNameEditText;
    /** Quantity of new item */
    private EditText mQuantityEditText;
    /** Price of new item */
    private EditText mPriceEditText;

    private ShoppingListAddListener mListener;

    private SharedPreferences mSharedPreferences;

    public ShoppingListAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListAddFragment.
     */
    public static ShoppingListAddFragment newInstance(String param1, String param2) {
        ShoppingListAddFragment fragment = new ShoppingListAddFragment();
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

        getActivity().findViewById(R.id.sliding_tabs_landing).setVisibility(View.GONE);

        getActivity().setTitle("Add Item");

        View v = inflater.inflate(R.layout.fragment_shopping_list_add, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_shop_item_name);
        mQuantityEditText = (EditText) v.findViewById(R.id.add_shop_item_quantity);
        mPriceEditText = (EditText) v.findViewById(R.id.add_shop_item_price);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        EditText search = (EditText) getActivity().findViewById(R.id.searchView);
        search.setVisibility(View.GONE);

        Button addItemButton = (Button) v.findViewById(R.id.shop_add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildURL(v);
                mListener.addShoppingItem(url);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.show();

        getActivity().findViewById(R.id.sliding_tabs_landing).setVisibility(View.VISIBLE);
        getActivity().setTitle("What's in My Fridge?");

        EditText search = (EditText) getActivity().findViewById(R.id.searchView);
        search.setVisibility(View.VISIBLE);
    }

    private String buildURL(View v) {

        StringBuilder sb = new StringBuilder(SHOPPING_ITEM_ADD_URL);

        try {

            mSharedPreferences = getActivity().getApplicationContext().
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String itemName = mNameEditText.getText().toString();
            sb.append("name=");
            sb.append(URLEncoder.encode(itemName, "UTF-8"));


            String itemQuantity = mQuantityEditText.getText().toString();
            sb.append("&quantity=");
            sb.append(URLEncoder.encode(itemQuantity, "UTF-8"));


            String itemPrice = mPriceEditText.getText().toString();
            sb.append("&price=");
            sb.append(URLEncoder.encode(itemPrice, "UTF-8"));

            String user = mSharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            Log.i("ShoppingListAddFragment", sb.toString());

        }
        catch(Exception e) {

        }
        return sb.toString();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShoppingListAddListener) {
            mListener = (ShoppingListAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingListAddListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ShoppingListAddListener {
        void addShoppingItem(String url);
    }
}
