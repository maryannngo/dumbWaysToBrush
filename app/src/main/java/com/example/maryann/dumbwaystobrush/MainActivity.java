package com.example.maryann.dumbwaystobrush;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oralb.sdk.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OBTBrushListener {

    private OBTBrushAdapter obtBrushAdapter;
    private MyOBTAuthListener listener;
    private MyOBTCloudListener callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.d("main", "before try block");

        try {
            //Call to initialize the OBTSDK
            OBTSDK.initialize(this);
            Log.d("main", "OBT SDK initialized");
            OBTSDK.authorizeSdk(new MyOBTSdkAuthListener());

            /*try {
                // Creating a ListView to show nearby Oral-B Toothbrushes
                ListView nearbyOralBToothbrushList = (ListView) findViewById(R.id.found_brushes_list);
                Log.d("main", "List View created");

                // Instantiate a custom adapter to hold the Oral-B Toothbrushes and set it to the list
                obtBrushAdapter = new OBTBrushAdapter();
                nearbyOralBToothbrushList.setAdapter(obtBrushAdapter);

                Log.d("main", "Custom adapter instantiated");
            /*
            listener = new MyOBTAuthListener();
            OBTSDK.authorizeApplication(listener);

            Log.d("main", "auth listener");
            */
                // Set OnItemClickListener to the ListView
            /*    nearbyOralBToothbrushList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Connect to chosen Oral-B Toothbrush
                        OBTSDK.connectToothbrush(obtBrushAdapter.getItem(position), true);
                    }
                });

                Log.d("main", "click listener attached");

                if (OBTSDK.isBluetoothAvailableAndEnabled())
                {
                    Log.d("checking Bluetooth", "true");
                    //OBTSDK.startScanning();
                }
                else Log.d("checking Bluetooth", "false");
            }
            catch (Exception e) {
                e.printStackTrace();
            }*/

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set this activity as OBTBrushListener
        OBTSDK.setOBTBrushListener(this);
        Log.d("onResume", "listener attached");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove the OBTBrushListener
        OBTSDK.setOBTBrushListener(null);
        Log.d("onPause", "listener attached");
    }

    @Override
    public void onNearbyBrushesFoundOrUpdated(List<OBTBrush> nearbyBrushes) {
        // Clear the custom adapter and set found Oral-B Toothbrushes as items
        Log.d("listener", "nearby brushes found or updated 1");
        obtBrushAdapter.clear();
        obtBrushAdapter.addAll(nearbyBrushes);
        obtBrushAdapter.notifyDataSetChanged();
        Log.d("listener", "nearby brushes found or updated 2");
    }

    @Override
    public void onBluetoothError() {
        //show notification in ui
        Log.d("main", "bluetooth error");
    }

    @Override
    public void onBrushDisconnected() {
        //show notification in ui
        Log.d("main", "brush disconnected");
    }

    @Override
    public void onBrushConnected() {
        //show notification in ui
        Log.d("main", "brush connected");
    }

    @Override
    public void onBrushConnecting() {
        //show notification in ui
        Log.d("main", "brush connecting");
    }

    @Override
    public void onBrushingTimeChanged(long l) {
        //show notification in ui
        Log.d("main", "brushing time changed");
    }

    @Override
    public void onBrushingModeChanged(int i) {
        //show notification in ui
        Log.d("main", "brushing mode changed");
    }

    @Override
    public void onBrushStateChanged(int i) {
        //show notification in ui
        Log.d("main", "brush state changed");
    }

    @Override
    public void onRSSIChanged(int i) {
        //show notification in ui
        Log.d("main", "rssi changed");
    }

    @Override
    public void onBatteryLevelChanged(float v) {
        //show notification in ui
        Log.d("main", "battery level changed");
    }

    @Override
    public void onSectorChanged(int i) {
        //move monsters to new sector
        Log.d("main", "sector changed");
    }

    @Override
    public void onHighPressureChanged(boolean b) {
        //show notification in ui
        Log.d("main", "high pressure changed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OBTBrushAdapter extends BaseAdapter {
        private List<OBTBrush> nearbyBrushes = new ArrayList<OBTBrush>();
        private LayoutInflater mInflater;

        public OBTBrushAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final OBTBrush item) {
            nearbyBrushes.add(item);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_type1, null);
                holder.textView = (TextView)convertView.findViewById(R.id.list_item_type1_text_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(nearbyBrushes.get(position).getName());
            return convertView;
        }

        @Override
        public OBTBrush getItem(int position) {
            return nearbyBrushes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return nearbyBrushes.size();
        }

        public void clear() {
            nearbyBrushes.clear();
        }

        public void addAll(List<OBTBrush> nb) {
            nearbyBrushes = nb;
        }
    }

    public static class ViewHolder {
        public TextView textView;
    }

    private class MyOBTAuthListener implements OBTUserAuthorizationListener {

        @Override
        public void onUserAuthorizationSuccess() {
            //show notification in ui
            Calendar cal = Calendar.getInstance();
            cal.set(2000, 1, 1);
            OBTSDK.fetchUserSessions(cal.getTimeInMillis(), callback);
        }

        @Override
        public void onUserAuthorizationFailed(int i) {
            //show notification in ui
        }
    }

    private class MyOBTCloudListener implements OBTCloudListener {

        @Override
        public void onSessionsLoaded(List<OBTSession> list) {
            //show in ui
        }

        @Override
        public void onFailure(int i) {
            //show in ui
        }
    }

    private class MyOBTSdkAuthListener implements OBTSdkAuthorizationListener {

        @Override
        public void onSdkAuthorizationSuccess() {
            Log.d("MyOBTSdkAuthListener", "success");

            try {
                // Creating a ListView to show nearby Oral-B Toothbrushes
                ListView nearbyOralBToothbrushList = (ListView) findViewById(R.id.found_brushes_list);
                Log.d("main", "List View created");

                // Instantiate a custom adapter to hold the Oral-B Toothbrushes and set it to the list
                obtBrushAdapter = new OBTBrushAdapter();
                nearbyOralBToothbrushList.setAdapter(obtBrushAdapter);

                Log.d("main", "Custom adapter instantiated");
                /*
                listener = new MyOBTAuthListener();
                OBTSDK.authorizeApplication(listener);

                Log.d("main", "auth listener");
                */
                // Set OnItemClickListener to the ListView
                nearbyOralBToothbrushList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Connect to chosen Oral-B Toothbrush
                        OBTSDK.connectToothbrush(obtBrushAdapter.getItem(position), false);
                    }
                });

                Log.d("main", "click listener attached");

                if (OBTSDK.isBluetoothAvailableAndEnabled())
                {
                    Log.d("checking Bluetooth", "true");
                    OBTSDK.startScanning();
                }
                else Log.d("checking Bluetooth", "false");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSdkAuthorizationFailed(int i) {
            Log.d("MyOBTSdkAuthListener", "failed");
        }
    }
}
