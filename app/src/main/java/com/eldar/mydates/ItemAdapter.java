package com.eldar.mydates;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

/**
 * Provide data to the list UI element items.
 */
public class ItemAdapter extends BaseAdapter {
    private static final String LOG_TAG = ItemAdapter.class.getCanonicalName();
    private final Context context;
    private final LayoutInflater layoutInflater;

    private Vector<SpecialDate> dates;

    public ItemAdapter(Context c) {
        context = c;
        layoutInflater = LayoutInflater.from(context);
        dates = new Vector<SpecialDate>();
        // Fixed list for now, we will learn to load and save it later.
        // Feel free to put your own dates here.
        dates.add(new SpecialDate("G", "2010/09/27 09:00:00"));
        dates.add(new SpecialDate("Z", "2012/10/01 10:00:00"));
        dates.add(new SpecialDate("M", "2000/04/28 08:00:00"));
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Log.d(LOG_TAG, String.format("getView() for item %d", i));
        LinearLayout itemView;
        if (view == null) { // Create a new view if no recycled view is available
            itemView = (LinearLayout) layoutInflater.inflate(
                    R.layout.list_item, parent, false /* attachToRoot */);
        } else {
            itemView = (LinearLayout) view;
        }
        TextView textView = (TextView)itemView.findViewById(R.id.textLabel);
        textView.setText(dates.elementAt(i).getLabel() + ": " + dates.elementAt(i).toString());
        textView = (TextView)itemView.findViewById(R.id.textValue);
        // textView.setText(String.format(parent.getResources().getString(R.string.ItemSampleText), i));
        textView.setText(dates.elementAt(i).timeSince());

        return itemView;
    }
}
