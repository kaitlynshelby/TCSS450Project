package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingItemDetailsFragment;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnShoppingListEditInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListEditFragment extends Fragment {

    private final static String ITEM_EDIT_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/editShoppingItem.php?";


    private TextView mNameTextView;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;

    private SharedPreferences mSharedPreferences;

    private OnShoppingListEditInteractionListener mListener;

    public ShoppingListEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InventoryEditFragment.
     */
    public static ShoppingListEditFragment newInstance() {
        ShoppingListEditFragment fragment = new ShoppingListEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping_list_edit, container, false);

        Bundle args = getArguments();
        ShoppingListItem inventoryItem = (ShoppingListItem) args.getSerializable(ShoppingItemDetailsFragment.SHOPPING_ITEM_SELECTED);

        mNameTextView = (TextView) v.findViewById(R.id.edit_shop_item_name);
        mNameTextView.setText(inventoryItem.getName());
        mPriceEditText = (EditText) v.findViewById(R.id.edit_shop_item_price);
        mPriceEditText.setText(inventoryItem.getPrice());
        mQuantityEditText = (EditText) v.findViewById(R.id.edit_shop_item_quantity);
        mQuantityEditText.setText(inventoryItem.getQuantity());


        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button editButton = (Button) v.findViewById(R.id.shop_edit_item_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildEditURL(v);
                mListener.editShoppingItem(url);
                EditItemTask task = new EditItemTask();
                task.execute(new String[] {url});


            }
        });

        return v;
    }

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


            mSharedPreferences = getActivity().getApplicationContext().
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = mSharedPreferences.getString("user", "");
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingEditFragment", sb.toString());

        }
        catch(Exception e) {
        }
        return sb.toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListEditInteractionListener) {
            mListener = (OnShoppingListEditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShoppingListEditInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
                            , Toast.LENGTH_LONG)
                            .show();
                    getActivity().getSupportFragmentManager().popBackStack(null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getActivity(), "Failed to edit item. Check your connection and try again."
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Failed to edit item. Please enter valid data."
                        , Toast.LENGTH_LONG)
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
    public interface OnShoppingListEditInteractionListener {
        void editShoppingItem(String url);
    }
}