package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    // TODO: Rename and change types and number of parameters
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
        View v = inflater.inflate(R.layout.fragment_inventory_add, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.add_item_name);
        mQuantityEditText = (EditText) v.findViewById(R.id.add_item_quantity);
        mPriceEditText = (EditText) v.findViewById(R.id.add_item_price);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button addItemButton = (Button) v.findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = buildCourseURL(v);
                // mListener.addShoppingItem(url);
            }
        });

        return v;
    }

    //private String buildURL


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
