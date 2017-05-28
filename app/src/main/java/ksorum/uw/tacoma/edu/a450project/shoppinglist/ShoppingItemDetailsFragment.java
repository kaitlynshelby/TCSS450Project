package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.ShoppingListEditFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingItemDetailsFragment extends Fragment {

    /** Shopping item selected */
    public final static String SHOPPING_ITEM_SELECTED = "shopping_item_selected";

    /** Item name */
    private TextView mNameTextView;
    /** Item quantity */
    private TextView mQuantityTextView;
    /** Item price */
    private TextView mPriceTextView;
    /** Item expiration */
    private ShoppingDetailsFragmentInteractionListener mListener;

    public ShoppingItemDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShoppingItemDetailsFragment.
     */
    public static ShoppingItemDetailsFragment newInstance() {
        ShoppingItemDetailsFragment fragment = new ShoppingItemDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_shopping_item_details, container, false);

        mNameTextView = (TextView) v.findViewById(R.id.shop_name_info);
        mQuantityTextView = (TextView) v.findViewById(R.id.shop_quantity_info);
        mPriceTextView = (TextView) v.findViewById(R.id.shop_price_info);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        ImageButton editButton = (ImageButton) v.findViewById(R.id.shop_detail_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListEditFragment fragment = new ShoppingListEditFragment();
                Bundle thisArgs = getArguments();
                ShoppingListItem item = (ShoppingListItem) thisArgs.getSerializable(SHOPPING_ITEM_SELECTED);

                Bundle editArgs = new Bundle();
                editArgs.putSerializable(SHOPPING_ITEM_SELECTED, item);
                fragment.setArguments(editArgs);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.shopping_list_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return v;
    }

    /**
     * Updates the fragment with details of the item that is clicked on.
     * @param item the item that is clicked on in the inventory list
     */
    public void updateView(ShoppingListItem item) {
        if (item != null) {
            mNameTextView.setText(item.getName());
            mQuantityTextView.setText(item.getQuantity());
            mPriceTextView.setText(item.getPrice());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((ShoppingListItem) args.getSerializable(SHOPPING_ITEM_SELECTED));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShoppingDetailsFragmentInteractionListener) {
            mListener = (ShoppingDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShoppingDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.show();
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
    public interface ShoppingDetailsFragmentInteractionListener {
        void onShoppingDetailsInteraction(Uri uri);
    }
}
