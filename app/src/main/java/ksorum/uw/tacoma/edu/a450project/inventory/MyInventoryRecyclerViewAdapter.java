package ksorum.uw.tacoma.edu.a450project.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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


    /**
     * Adapter constructor.
     *
     * @param items    the list of inventory items
     * @param listener the fragment listener for the list
     */
    public MyInventoryRecyclerViewAdapter(Context context, List<InventoryItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

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
        
        String itemExpiration = mValues.get(pos).getExpiration();
        System.out.println("Item's Expiration Date: " + itemExpiration);
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
            Log.i("CHECK", "Date parsed and formatted");

            long diff = Math.round((itemDate.getTime() - compareDate.getTime()) / (double) 86400000);


            int difference = (int) diff;
            System.out.println("Days difference: " + difference);

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



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageButton mDeleteView;
        public InventoryItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_name);
            mDeleteView = (ImageButton) view.findViewById(R.id.delete_button);
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
