package com.eldar.mydates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

/**
 * Provide data to the list UI element items.
 */
public class ItemAdapter extends BaseAdapter {
    private static final String LOG_TAG = ItemAdapter.class.getCanonicalName();
    private static final String dataFileName = "data_my_dates.txt";
    private final Context context;
    private final LayoutInflater layoutInflater;

    private Vector<SpecialDate> dates;

    public ItemAdapter(Context c) {
        context = c;
        layoutInflater = LayoutInflater.from(context);
        dates = new Vector<SpecialDate>();
    }

    public void setDates(Vector<SpecialDate> dates) {
        if (dates != null) {
            this.dates = dates;
            notifyDataSetChanged();
        }
    }

    public void saveDates() {
        try {
            FileOutputStream outputStream =
                    context.openFileOutput(dataFileName, Context.MODE_PRIVATE);
            SpecialDate.writeDatesList(outputStream, dates);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDates() {
        try {
            FileInputStream is = context.openFileInput(dataFileName);
            setDates(SpecialDate.readDatesList(is));
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDate(SpecialDate date) {
        loadDates();
        dates.add(date);
        saveDates();
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
        //Log.d(LOG_TAG, String.format("getView() for item %d", i));
        LinearLayout itemView;
        if (view == null) { // Create a new view if no recycled view is available
            itemView = (LinearLayout) layoutInflater.inflate(
                    R.layout.list_item, parent, false /* attachToRoot */);
        } else {
            itemView = (LinearLayout) view;
        }
        TextView textView = (TextView)itemView.findViewById(R.id.textLabel);
        textView.setText(dates.elementAt(i).getLabel());

        textView = (TextView)itemView.findViewById(R.id.textDate);
        textView.setText(dates.elementAt(i).toString());

        textView = (TextView)itemView.findViewById(R.id.textValue);
        textView.setText(dates.elementAt(i).timeSince());

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:  // Touch.
                        TextView textView = (TextView)view.findViewById(R.id.textLabel);
                        new AlertDialog.Builder(context)
                                .setTitle("Deleting the date?")
                                .setMessage("'Yes' to delete " + textView.getText())
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Toast.makeText(context, "Deleting", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Toast.makeText(context, "Not deleting", Toast.LENGTH_LONG)
                                             .show();
                                    }
                                })
                                .create()
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:  // Move, un-touch: nothing to do.
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });

        return itemView;
    }
}
