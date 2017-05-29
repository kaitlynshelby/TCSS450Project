package ksorum.uw.tacoma.edu.a450project.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.inventoryitem.InventoryItem;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem;

import java.util.ArrayList;
import java.util.List;

import static ksorum.uw.tacoma.edu.a450project.R.drawable.waste_bin;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ksorum.uw.tacoma.edu.a450project.shoppinglist.shoppinglistitem.ShoppingListItem} and makes a call to the
 * specified {@link ShoppingListFragment.OnShoppingListFragmentInteractionListener}.
 */
public class MyShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<MyShoppingListRecyclerViewAdapter.ViewHolder> {

    /** The list of inventory items */
    private final List<ShoppingListItem> mValues;
    /** Fragment listener in the list */
    private final ShoppingListFragment.OnShoppingListFragmentInteractionListener mListener;

    private final OnDeleteItem mDeleteListener;

    private List<ShoppingListItem> mValuesCopy;


    private final Activity mContext;

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
        final int pos = position;
        holder.mItem = mValues.get(position);
        String text = mValues.get(position).getName() + " (" + mValues.get(position).getQuantity() + ")";
        holder.mIdView.setText(text);
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

    }

    public void filter(String text) {
        mValues.clear();
        if(text.isEmpty()){
            mValues.addAll(mValuesCopy);
        } else{
            text = text.toLowerCase();
            for(int i = 0; i < mValuesCopy.size(); i++){
                if(mValuesCopy.get(i).getName().toLowerCase().contains(text)){
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


    public interface OnDeleteItem {
        boolean deleteShopItem(String id, String name, String quantity, String price);
    }
}
