package ksorum.uw.tacoma.edu.a450project;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private InventoryAddListener mListener;

    private EditText mItemNameEditText;
    private EditText mItemQuantityEditText;
    private EditText mItemPriceEditText;
    private EditText mItemExpirationEditText;

    private final static String INVENTORY_ITEM_ADD_URL
            = "http://cssgate.insttech.washington.edu/~jazzyd25/Android/addInventoryItem.php?";


    public InventoryAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryAddFragment newInstance(String param1, String param2) {
        InventoryAddFragment fragment = new InventoryAddFragment();
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
        }
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

    public interface InventoryAddListener {
        public void addItem(String url);
    }

    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(INVENTORY_ITEM_ADD_URL);

        try {

            String itemName = mItemNameEditText.getText().toString();
            sb.append("itemName=");
            sb.append(itemName);


            String itemQuantity = mItemQuantityEditText.getText().toString();
            sb.append("&quantity=");
            sb.append(URLEncoder.encode(itemQuantity, "UTF-8"));


            String itemPrice = mItemPriceEditText.getText().toString();
            sb.append("&price=");
            sb.append(URLEncoder.encode(itemPrice, "UTF-8"));

            String itemExpiration = mItemExpirationEditText.getText().toString();
            sb.append("&expiration=");
            sb.append(URLEncoder.encode(itemExpiration, "UTF-8"));

            Log.i("InventoryAddFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

}
