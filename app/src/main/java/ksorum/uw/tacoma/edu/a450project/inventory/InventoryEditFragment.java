package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

/**
 * A fragment to edit inventory items.
 *
 * Activities that contain this fragment must implement the
 * {@link OnInventoryEditInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryEditFragment extends Fragment {

    /**
     * URL of the location to edit an item in the inventory
     */
    private final static String ITEM_EDIT_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/editInventoryItem.php?";

    /**
     * Edit name field
     */
    private EditText mNameTextView;
    /**
     * Edit price field
     */
    private EditText mPriceEditText;
    /**
     * Edit quantity field
     */
    private EditText mQuantityEditText;
    /**
     * Edit expiration date field
     */
    private EditText mExpirationEditText;
    /**
     * The ID of the item
     */
    private String mItemId;

    /**
     * Listener to edit an item's information
     */
    private OnInventoryEditInteractionListener mListener;

    public InventoryEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InventoryEditFragment.
     */
    public static InventoryEditFragment newInstance() {
        InventoryEditFragment fragment = new InventoryEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.sliding_tabs_landing).setVisibility(View.GONE);

        getActivity().setTitle("Edit Item");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory_edit, container, false);

        Bundle args = getArguments();
        InventoryItem inventoryItem = (InventoryItem) args.getSerializable(InventoryItemDetailsFragment.INVENTORY_ITEM_SELECTED);

        // Set the current item details in the text fields for easy editing
        mNameTextView = (EditText) v.findViewById(R.id.edit_item_name);
        mNameTextView.setText(inventoryItem.getItemName());
        mPriceEditText = (EditText) v.findViewById(R.id.edit_item_price);
        mPriceEditText.setText(inventoryItem.getPrice());
        mQuantityEditText = (EditText) v.findViewById(R.id.edit_item_quantity);
        mQuantityEditText.setText(inventoryItem.getQuantity());
        mExpirationEditText = (EditText) v.findViewById(R.id.edit_item_expiration);
        mExpirationEditText.setText(inventoryItem.getExpiration());
        mItemId = inventoryItem.getId();


        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        EditText search = (EditText) getActivity().findViewById(R.id.searchView);
        search.setVisibility(View.GONE);

        Button editButton = (Button) v.findViewById(R.id.edit_item_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    String url = buildEditURL(v);
                    EditItemTask task = new EditItemTask();
                    task.execute(new String[]{url});
                } else {
                    Toast.makeText(getActivity(),
                            "Cannot edit item without a network connection.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }


    /**
     * Reads the user input of an item and updates its information.
     *
     * @param v the view of the fragment
     * @return the string of the URL
     */
    private String buildEditURL(View v) {

        StringBuilder sb = new StringBuilder(ITEM_EDIT_URL);

        try {

            String name = mNameTextView.getText().toString();
            sb.append("name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));


            String price = mPriceEditText.getText().toString();
            sb.append("&price=");
            sb.append(URLEncoder.encode(price, "UTF-8"));


            String quantity = mQuantityEditText.getText().toString();
            sb.append("&quantity=");
            sb.append(URLEncoder.encode(quantity, "UTF-8"));

            String exp = mExpirationEditText.getText().toString();
            sb.append("&expiration=");
            sb.append(URLEncoder.encode(exp, "UTF-8"));

            sb.append("&id=");
            sb.append(URLEncoder.encode(mItemId, "UTF-8"));

            Log.i("InventoryEditFragment", sb.toString());

        } catch (Exception e) {
        }
        return sb.toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInventoryEditInteractionListener) {
            mListener = (OnInventoryEditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInventoryEditInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * Shows all view elements that were hidden in OnCreateView
     */
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
     * Launches the web services to update an item's information.
     */
    private class EditItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getActivity(), "Item was edited"
                            , Toast.LENGTH_SHORT)
                            .show();
                    getActivity().getSupportFragmentManager().popBackStack(null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getActivity(), "Failed to edit item. Check your connection and try again."
                            , Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Failed to edit item. Please enter valid data."
                        , Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
    public interface OnInventoryEditInteractionListener {
        void editInventoryItem(String url);
    }
}
