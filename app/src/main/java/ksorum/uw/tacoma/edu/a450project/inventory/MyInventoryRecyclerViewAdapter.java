package ksorum.uw.tacoma.edu.a450project.inventory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment.OnListFragmentInteractionListener;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ksorum.uw.tacoma.edu.a450project.R.drawable.delete_icon;
import static ksorum.uw.tacoma.edu.a450project.R.drawable.waste_bin;

/**
 * {@link RecyclerView.Adapter} that can display a {@link InventoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyInventoryRecyclerViewAdapter extends RecyclerView.Adapter<MyInventoryRecyclerViewAdapter.ViewHolder> {

    /**
     * The list of inventory items
     */
    private final List<InventoryItem> mValues;
    /**
     * Fragment listener in the list
     */
    private final OnListFragmentInteractionListener mListener;

    private final OnDeleteItem mDeleteListener;

    private static final String URL =
            "http://cssgate.insttech.washington.edu/~ksorum/deleteInventoryItem.php?";

    private List<InventoryItem> mValuesCopy;

    private Activity mContext;




    /**
     * Adapter constructor.
     *
     * @param items    the list of inventory items
     * @param listener the fragment listener for the list
     */
    public MyInventoryRecyclerViewAdapter(Context context, List<InventoryItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = (Activity) context;

        mValuesCopy = new ArrayList<InventoryItem>();
        mValuesCopy.addAll(mValues);

        if (context instanceof OnDeleteItem) {
            mDeleteListener = (OnDeleteItem) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeleteListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        holder.mItem = mValues.get(position);
        String text = mValues.get(position).getItemName() + " (" + mValues.get(position).getQuantity() + ")";
        holder.mIdView.setText(text);
        holder.mDeleteView.setBackgroundResource(0);
        holder.mDeleteView.setImageResource(waste_bin);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean deleted = mDeleteListener.deleteItem(holder.mItem.getItemName(),
                        holder.mItem.getQuantity(), holder.mItem.getPrice());

                if (deleted) {
                    mValues.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mValues.size());
                }

            }
        });


        holder.mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                filter(s);
            }
        });

        // Color-coding system for expiration dates

        holder.mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                filter(s);
            }
        });
        
        String itemExpiration = mValues.get(pos).getExpiration();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date itemDate;
        Date compareDate;

        // Today's date
        Date todaysDate = new Date();
        DateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = todaysDateFormat.format(todaysDate);


        try {
            itemDate = df.parse(itemExpiration);
            compareDate = df.parse(today);

            long diff = Math.round((itemDate.getTime() - compareDate.getTime()) / (double) 86400000);


            int difference = (int) diff;

            Log.i("item", holder.mItem.getItemName());

            if (difference <= 1) {
                holder.mView.setBackgroundColor(Color.RED);
                holder.mView.getBackground().setAlpha(100);
            } else if (difference >= 2 && difference <= 3) {
                holder.mView.setBackgroundColor(Color.YELLOW);
                holder.mView.getBackground().setAlpha(100);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void filter(String text) {
        mValues.clear();
        if(text.isEmpty()){
            mValues.addAll(mValuesCopy);
        } else{
            text = text.toLowerCase();
            for(int i = 0; i < mValuesCopy.size(); i++){
                if(mValuesCopy.get(i).getItemName().toLowerCase().contains(text)){
                    mValues.add(mValuesCopy.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageButton mDeleteView;
        public InventoryItem mItem;
        public final EditText mSearchView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_name);
            mDeleteView = (ImageButton) view.findViewById(R.id.delete_button);
            mSearchView = (EditText) mContext.findViewById(R.id.searchView);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }


    public interface OnDeleteItem {
        boolean deleteItem(String name, String quantity, String price);
    }

}
