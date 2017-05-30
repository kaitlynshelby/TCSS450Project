package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.data.ShoppingItemsDB;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnShoppingListFragmentInteractionListener}
 * interface.
 */
public class ShoppingListFragment extends Fragment {

    private OnShoppingListFragmentInteractionListener mListener;
    public static final String LIST_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/shoppinglist.php?cmd=items";
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;

    private ShoppingItemsDB mShoppingItemsDB;
    private List<ShoppingListItem> mShoppingListItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingListFragment() {
    }

    public static ShoppingListFragment newInstance(int columnCount) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        setHasOptionsMenu(true);
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
                DownloadShoppingItemsTask task = new DownloadShoppingItemsTask();
                String url = buildShoppingListGetURL(view);
                task.execute(new String[]{url});
                Log.i("executed task", "");
            }
            else {
                Toast.makeText(view.getContext(),
                        "No network connection available. Displaying locally stored data",
                        Toast.LENGTH_SHORT).show();
                if (mShoppingItemsDB == null) {
                    mShoppingItemsDB = new ShoppingItemsDB(getActivity());
                }
                if (mShoppingListItems == null) {
                    mShoppingListItems = mShoppingItemsDB.getItems();
                }
                mRecyclerView.setAdapter(new MyShoppingListRecyclerViewAdapter(getActivity(),
                        mShoppingListItems, mListener));
            }


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListFragmentInteractionListener) {
            mListener = (OnShoppingListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShoppingListFragmentInteractionListener");
        }
    }


   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String text = shoppingListItemsToString();
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String shoppingListItemsToString() {
        StringBuilder sb = new StringBuilder();

        // format: Name xQuantity
        String name;
        String quantity;

        for (int i = 0; i < mShoppingListItems.size(); i++) {
            name = mShoppingListItems.get(i).getName();
            quantity = mShoppingListItems.get(i).getQuantity();
            sb.append(name);
            sb.append(" x");
            sb.append(quantity);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Build the url which will be used by the webservice.
     *
     * @param v the View object
     * @return the url to be used by the webservice
     */
    private String buildShoppingListGetURL(View v) {

        StringBuilder sb = new StringBuilder(LIST_URL);

        try {

            mSharedPreferences = getActivity().getApplicationContext().
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = mSharedPreferences.getString("user", "");

            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("ShoppingListFragment", sb.toString());

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
    public interface OnShoppingListFragmentInteractionListener {
        void onShoppingListFragmentInteraction(ShoppingListItem item);
    }

    private class DownloadShoppingItemsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection)
                            urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to retrieve your items." +
                        " Please check your connection and try again.", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            mShoppingListItems = new ArrayList<ShoppingListItem>();
            result = ShoppingListItem.parseListJSON(result, mShoppingListItems);
            Log.i("OnPostExecute", mShoppingListItems.get(0).getName());
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            try {
                // Everything is good, show the list of courses.
                if (!mShoppingListItems.isEmpty()) {

                    if (mShoppingItemsDB == null) {
                        mShoppingItemsDB = new ShoppingItemsDB(getActivity());
                    }

                    // Delete old data so that you can refresh the local
                    // database with the network data.
                    mShoppingItemsDB.deleteItems();


                    // Also, add to the local database
                    for (int i=0; i<mShoppingListItems.size(); i++) {
                        ShoppingListItem item = mShoppingListItems.get(i);
                        mShoppingItemsDB.insertShoppingItem(item.getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice());
                    }

                    mRecyclerView.setAdapter(new MyShoppingListRecyclerViewAdapter(getActivity(),
                            mShoppingListItems, mListener));
                }
            } catch (NullPointerException e) {

            }

        }
     }
}
