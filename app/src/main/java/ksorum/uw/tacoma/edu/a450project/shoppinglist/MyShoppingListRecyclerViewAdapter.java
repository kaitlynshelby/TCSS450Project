package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.app.Activity;
import android.content.Context;
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
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

import static ksorum.uw.tacoma.edu.a450project.R.drawable.waste_bin;

/**
 * RecyclerView.Adapter for ShoppingListFragment
 * <p>
 * {@link RecyclerView.Adapter} that can display a {@link ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem} and makes a call to the
 * specified {@link ShoppingListFragment.OnShoppingListFragmentInteractionListener}.
 */
public class MyShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<MyShoppingListRecyclerViewAdapter.ViewHolder> {

    /**
     * The list of inventory items
     */
    private final List<ShoppingListItem> mValues;
    /**
     * Fragment listener in the list
     */
    private final ShoppingListFragment.OnShoppingListFragmentInteractionListener mListener;

    /**
     * Listener to delete item from shopping list
     */
    private final OnDeleteItem mDeleteListener;

    /**
     * Copy of shopping list items in list
     */
    private List<ShoppingListItem> mValuesCopy;

    /**
     * Context of application
     */
    private final Activity mContext;

    /**
     * Constructor of RecyclerView for the shopping list
     *
     * @param context  context of application
     * @param items    list of shopping list items
     * @param listener listener for shopping list fragment
     */
    public MyShoppingListRecyclerViewAdapter(Context context, List<ShoppingListItem> items,
                                             ShoppingListFragment.OnShoppingListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = (Activity) context;

        mValuesCopy = new ArrayList<ShoppingListItem>();
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
                .inflate(R.layout.fragment_shopping, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the item at given position
        final int pos = position;
        holder.mItem = mValues.get(position);

        // set the text for the view holder
        String text = mValues.get(position).getName() + " (" + mValues.get(position).getQuantity() + ")";
        holder.mIdView.setText(text);

        // add delete image to view holder
        holder.mDeleteView.setBackgroundResource(0);
        holder.mDeleteView.setImageResource(waste_bin);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onShoppingListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean deleted = mDeleteListener.deleteShopItem(holder.mItem.getId(), holder.mItem.getName(),
                        holder.mItem.getQuantity(), holder.mItem.getPrice());

                // remove deleted item from list
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
                if (mValuesCopy.get(i).getName().toLowerCase().contains(text)) {
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
     * Initializes the views of an shopping list item row in a list.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageButton mDeleteView;
        public ShoppingListItem mItem;
        public final EditText mSearchView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.shop_item_name);
            mDeleteView = (ImageButton) view.findViewById(R.id.shop_delete_button);
            mSearchView = (EditText) mContext.findViewById(R.id.searchView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }


    /**
     * Interface to delete an item from the shopping list.
     */
    public interface OnDeleteItem {
        boolean deleteShopItem(String id, String name, String quantity, String price);
    }
}
