package ksorum.uw.tacoma.edu.a450project;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
//    public CustomAdapter(ArrayList<String> addedItems, Context context) {
//
//    }
}
