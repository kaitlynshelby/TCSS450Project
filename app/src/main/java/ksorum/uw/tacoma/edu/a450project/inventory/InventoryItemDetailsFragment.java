package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;


/**
 * A fragment that displays the details of an item.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class InventoryItemDetailsFragment extends Fragment {

    /**
     * Inventory item selected
     */
    public final static String INVENTORY_ITEM_SELECTED = "inventory_item_selected";

    /**
     * Item name
     */
    private TextView mItemNameTextView;
    /**
     * Item quantity
     */
    private TextView mItemQuantityTextView;
    /**
     * Item price
     */
    private TextView mItemPriceTextView;
    /**
     * Item expiration
     */
    private TextView mItemExpirationTextView;

    /**
     * Fragment listener
     */
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public InventoryItemDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InventoryItemDetailsFragment.
     */
    public static InventoryItemDetailsFragment newInstance() {
        InventoryItemDetailsFragment fragment = new InventoryItemDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Item Details");

        getActivity().findViewById(R.id.sliding_tabs_landing).setVisibility(View.GONE);

        View v = inflater.inflate(R.layout.fragment_inventory_item_details, container, false);

        mItemNameTextView = (TextView) v.findViewById(R.id.name_info);
        mItemQuantityTextView = (TextView) v.findViewById(R.id.quantity_info);
        mItemPriceTextView = (TextView) v.findViewById(R.id.price_info);
        mItemExpirationTextView = (TextView) v.findViewById(R.id.expiration_info);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        EditText search = (EditText) getActivity().findViewById(R.id.searchView);
        search.setVisibility(View.GONE);

        ImageButton editButton = (ImageButton) v.findViewById(R.id.inventory_detail_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryEditFragment fragment = new InventoryEditFragment();
                Bundle thisArgs = getArguments();
                InventoryItem item = (InventoryItem) thisArgs.getSerializable(INVENTORY_ITEM_SELECTED);

                Bundle editArgs = new Bundle();
                editArgs.putSerializable(INVENTORY_ITEM_SELECTED, item);
                fragment.setArguments(editArgs);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_landing, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }


    /**
     * Updates the fragment with details of the item that is clicked on.
     *
     * @param item the item that is clicked on in the inventory list
     */
    public void updateView(InventoryItem item) {
        if (item != null) {
            mItemNameTextView.setText(item.getItemName());
            mItemQuantityTextView.setText(item.getQuantity());
            mItemPriceTextView.setText(item.getPrice());
            mItemExpirationTextView.setText(item.getExpiration());
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((InventoryItem) args.getSerializable(INVENTORY_ITEM_SELECTED));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

        getActivity().findViewById(R.id.sliding_tabs_landing).setVisibility(View.VISIBLE);
        getActivity().setTitle("What's in My Fridge?");

        EditText search = (EditText) getActivity().findViewById(R.id.searchView);
        search.setVisibility(View.VISIBLE);
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
