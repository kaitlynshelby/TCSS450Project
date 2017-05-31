package ksorum.uw.tacoma.edu.a450project.inventory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.InventoryFragment.OnListFragmentInteractionListener;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;

import static ksorum.uw.tacoma.edu.a450project.R.drawable.waste_bin;

/**
 * RecyclerView.Adapter for InventoryFragment
 * <p>
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

    /**
     * Listener to delete items from the inventory
     */
    private final OnDeleteItem mDeleteListener;

    /**
     * Copy of inventory items in the list
     */
    private List<InventoryItem> mValuesCopy;

    /**
     * Context of application
     */
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
                boolean deleted = mDeleteListener.deleteInventoryItem(holder.mItem.getId(),
                        holder.mItem.getItemName(),
                        holder.mItem.getQuantity(), holder.mItem.getPrice());

                if (deleted) {
                    mValues.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mValues.size());
                }

                notifyDataSetChanged();
                ;

            }
        });


        // Color-coding expiration date system
        int color = holder.mItem.getColor();
        if (color != 0) {
            holder.mView.setBackgroundColor(color);
            holder.mView.getBackground().setAlpha(100);
        } else {
            holder.mView.setBackgroundColor(Color.TRANSPARENT);
        }


        holder.mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String s = charSequence.toString();
                filter(s);
            }

            /**
             * Listens for changes in the search bar to filter list as necessary
             * @param editable the search bar text
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                filter(s);
            }
        });

    }


    /**
     * Filters out items while the user types in the search bar.
     *
     * @param text text typed into search bar
     */
    public void filter(String text) {
        mValues.clear();
        if (text.isEmpty()) {
            mValues.addAll(mValuesCopy);
        } else {
            text = text.toLowerCase();
            for (int i = 0; i < mValuesCopy.size(); i++) {
                if (mValuesCopy.get(i).getItemName().toLowerCase().contains(text)) {
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

    /**
     * Initializes the views of an inventory item row in a list.
     */
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


    /**
     * Interface to delete an item from the inventory.
     */
    public interface OnDeleteItem {
        boolean deleteInventoryItem(String id, String name, String quantity, String price);
    }

}
