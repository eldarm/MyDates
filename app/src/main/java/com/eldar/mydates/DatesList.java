package com.eldar.mydates;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by eldar on 10/7/14.
 */
public class DatesList extends ActionBarActivity {
    private static final String LOG_TAG = DatesList.class.getCanonicalName();
    private static final String SHOW_ANNIVERSARY = "anniversary";
    private static final String SHOW_TIME_TILL = "time_till";
    private Timer timer;
    private final Handler timeHandler = new Handler();
    private ItemAdapter itemAdapter;

    //Fragment that displays the meme.
    public static class DatesListFragment extends Fragment {
        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_dates_list, container, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_list);

        GridView gridView = (GridView) findViewById(R.id.listView);
        itemAdapter = new ItemAdapter(this);
        gridView.setAdapter(itemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultShow = sharedPref.getString(getString(R.string.setting_show), SHOW_TIME_TILL);
        if (defaultShow.equals(SHOW_ANNIVERSARY)) {
            itemAdapter.setShowAnniversary(true);
        }
        itemAdapter.loadDates();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        itemAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 500, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dates_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_show) {
            // Switch view:
            boolean showAnniversary = !itemAdapter.getShowAnniversary();
            itemAdapter.setShowAnniversary(showAnniversary);

            // And don't forget to save the preferences:
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.setting_show),
                    showAnniversary ? SHOW_ANNIVERSARY : SHOW_TIME_TILL);
            editor.commit();
            return true;
        }
        if (id == R.id.action_help) {
            Toast.makeText(this, "Use Add a Date to add dates, click on a date to delete.",
                    Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        if (id == R.id.action_add_date) {
            Toast.makeText(this, "Adding a date...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DatesList.this, EnterDate.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
