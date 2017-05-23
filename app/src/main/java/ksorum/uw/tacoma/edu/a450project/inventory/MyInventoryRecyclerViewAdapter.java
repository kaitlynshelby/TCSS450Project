package ksorum.uw.tacoma.edu.a450project.inventory;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment.OnListFragmentInteractionListener;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

import java.util.List;

import static ksorum.uw.tacoma.edu.a450project.R.drawable.delete_button;
import static ksorum.uw.tacoma.edu.a450project.R.drawable.waste_bin;

/**
 * {@link RecyclerView.Adapter} that can display a {@link InventoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyInventoryRecyclerViewAdapter extends RecyclerView.Adapter<MyInventoryRecyclerViewAdapter.ViewHolder> {

    /** The list of inventory items */
    private final List<InventoryItem> mValues;
    /** Fragment listener in the list */
    private final OnListFragmentInteractionListener mListener;

    /**
     * Adapter constructor.
     * @param items the list of inventory items
     * @param listener the fragment listener for the list
     */
    public MyInventoryRecyclerViewAdapter(List<InventoryItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mIdView.setText(mValues.get(position).getItemName());
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
                mValues.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, mValues.size());
                Log.i("DELETE", "Click working");
            }
        });
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


    /**
     * Interface for deleting an item to the inventory.
     */
    public interface InventoryDeleteListener {
        public void deleteItem(String url);
    }
}
