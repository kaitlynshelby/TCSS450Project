package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.data.InventoryItemsDB;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class InventoryFragment extends Fragment {

    /** Number of columns */
    private static final String ARG_COLUMN_COUNT = "column-count";

    /** Number of columns */
    private int mColumnCount = 1;

    /** Listener for list items */
    private OnListFragmentInteractionListener mListener;

    /** The recycler view of the items */
    private RecyclerView mRecyclerView;

    /** URL for location of inventory items */
    private static final String ITEM_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/inventorylist.php?cmd=inventoryitems";

    private String mUserEmail;
    private SharedPreferences mSharedPreferences;

    private InventoryItemsDB mInventoryItemsDB;
    private List<InventoryItem> mInventoryItemList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InventoryFragment() {
    }

    /**
     * Creates a new instance of the inventory fragment.
     * @param columnCount number of columns for the list
     * @return a new instance of the inventory fragment.
     */
    public static InventoryFragment newInstance(int columnCount) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadItemsTask task = new DownloadItemsTask();
            String url = buildInventoryGetURL(view);
            task.execute(new String[]{url});
        }
        else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Displaying locally stored data",
                    Toast.LENGTH_SHORT).show();
            if (mInventoryItemsDB == null) {
                mInventoryItemsDB = new InventoryItemsDB(getActivity());
            }
            if (mInventoryItemList == null) {
                mInventoryItemList = mInventoryItemsDB.getItems();
            }
            mRecyclerView.setAdapter(new MyInventoryRecyclerViewAdapter(getActivity(),
                    mInventoryItemList, mListener));
        }

            return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Build the url which will be used by the webservice.
     *
     * @param v the View object
     * @return the url to be used by the webservice
     */
    private String buildInventoryGetURL(View v) {

        StringBuilder sb = new StringBuilder(ITEM_URL);

        try {

            mSharedPreferences = getActivity().getApplicationContext().
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            mUserEmail = mSharedPreferences.getString("user", "");

            String user = mUserEmail;
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("InventoryFragment", sb.toString());

        }
        catch(Exception e) {

        }
        return sb.toString();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(InventoryItem item);
    }

    /**
     * Launches the web services to display the inventory items.
     */
    private class DownloadItemsTask extends AsyncTask<String, Void, String> {


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
                    response = "Unable to download the list of items, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. Tries to call the parse Method and checks to see if it was successful.
         * If not, displays the exception.
         *
         * @param result the returned JSON formatted as a String
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to retrieve your items." +
                        " Please check your connection and try again.", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            mInventoryItemList = new ArrayList<InventoryItem>();
            result = InventoryItem.parseCourseJSON(result, mInventoryItemList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                if (!mInventoryItemList.isEmpty()) {

                    if (mInventoryItemsDB == null) {
                        mInventoryItemsDB = new InventoryItemsDB(getActivity());
                    }

                    // Delete old data so that you can refresh the local
                    // database with the network data.
                    mInventoryItemsDB.deleteItems();


                    // Also, add to the local database
                    for (int i=0; i<mInventoryItemList.size(); i++) {
                        InventoryItem item = mInventoryItemList.get(i);
                        mInventoryItemsDB.insertInventoryItem(item.getId(),
                                item.getItemName(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getExpiration());
                    }


                    getActivity().findViewById(R.id.searchView);
                    mRecyclerView.setAdapter(new MyInventoryRecyclerViewAdapter(getActivity(),
                            mInventoryItemList, mListener));
                }
            } catch (NullPointerException e){

            }

        }
    }


}
