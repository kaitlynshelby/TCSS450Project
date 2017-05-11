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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryItemDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryItemDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mItemNameTextView;
    private TextView mItemQuantityTextView;
    private TextView mItemPriceTextView;
    private TextView mItemExpirationTextView;

    private OnFragmentInteractionListener mListener;

    public InventoryItemDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryItemDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryItemDetailsFragment newInstance(String param1, String param2) {
        InventoryItemDetailsFragment fragment = new InventoryItemDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        floatingActionButton.show();


        return v;
    }

    public void updateView(InventoryItem item) {
        if (item != null) {
            mItemNameTextView.setText(item.getItemName());
            mItemQuantityTextView.setText(item.getQuantity());
            mItemPriceTextView.setText(item.getPrice());
            mItemExpirationTextView.setText(item.getExpiration());
        }
    }

    public final static String INVENTORY_ITEM_SELECTED = "inventory_item_selected";

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
