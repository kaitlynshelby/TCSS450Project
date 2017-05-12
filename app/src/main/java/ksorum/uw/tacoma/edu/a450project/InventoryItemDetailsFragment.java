package ksorum.uw.tacoma.edu.a450project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ksorum.uw.tacoma.edu.a450project.inventoryitem.InventoryItem;


/**
 * A fragment that displays the details of an item.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class InventoryItemDetailsFragment extends Fragment {

    /** Inventory item selected */
    public final static String INVENTORY_ITEM_SELECTED = "inventory_item_selected";

    /** Item name */
    private TextView mItemNameTextView;
    /** Item quantity */
    private TextView mItemQuantityTextView;
    /** Item price */
    private TextView mItemPriceTextView;
    /** Item expiration */
    private TextView mItemExpirationTextView;

    /** Fragment listener */
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public InventoryItemDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment InventoryItemDetailsFragment.
     */
    public static InventoryItemDetailsFragment newInstance() {
        InventoryItemDetailsFragment fragment = new InventoryItemDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_inventory_item_details, container, false);

        mItemNameTextView = (TextView) v.findViewById(R.id.name_info);
        mItemQuantityTextView = (TextView) v.findViewById(R.id.quantity_info);
        mItemPriceTextView = (TextView) v.findViewById(R.id.price_info);
        mItemExpirationTextView = (TextView) v.findViewById(R.id.expiration_info);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();


        return v;
    }

    /**
     * Updates the fragment with details of the item that is clicked on.
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
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
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
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
