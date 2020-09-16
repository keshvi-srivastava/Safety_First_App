package com.example.android.safetyfirst;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static android.R.attr.action;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int i=0;
    private LocationListener locationListener;
    private TextView latitudeField;
    private TextView longitudeField;
    private LocationManager locationManager;
    Location lm;
    double lat, lng;
    String message1, message2;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        latitudeField = (TextView) findViewById(R.id.Lat);
        longitudeField = (TextView) findViewById(R.id.Long);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        lm = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lm != null) {
            latitudeField.setText(String.valueOf(lat));
            longitudeField.setText(String.valueOf(lng));
            lat = lm.getLatitude();
            lng = lm.getLongitude();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location lm) {
                lat = lm.getLatitude();
                lng = lm.getLongitude();
                latitudeField.setText(String.valueOf(lat));
                longitudeField.setText(String.valueOf(lng));
                message1 = ("Please help! I'm at this location. (Google maps co-ordinates, copy and paste on google maps)");
                message2 = (lat + "," + lng);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getApplicationContext());
                    ArrayList<Finding> findings = dbAdapter.getFindings();
                    for (int i = 1; i <= 3; i++) {
                        Finding cn = findings.get(findings.size() - i);
                        smsManager.sendTextMessage(cn.getAddress(), null, message1, null, null);
                        smsManager.sendTextMessage(cn.getAddress(), null, message2, null, null);
                    }
                    Snackbar.make(view, "Message sent", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d("---", "Sent");
                } catch (Exception e) {
                    Snackbar.make(view, "Message not sent", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d("---", "Fail");
                    e.printStackTrace();
                }
            }
        });
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mBtAdapter.startDiscovery();
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceName = device.getName();
                       // String deviceAddress = device.getAddress();
                        if (deviceName.equals("OnePlus3"))
                        {
                            Toast.makeText(getApplicationContext(), "Found it!", Toast.LENGTH_LONG).show();
                            SmsManager smsManager = SmsManager.getDefault();
                            KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getApplicationContext());
                            ArrayList<Finding> findings = dbAdapter.getFindings();

                            for (int i = 1; i <= 3; i++) {
                                Finding cn = findings.get(findings.size() - i);
                                smsManager.sendTextMessage(cn.getAddress(), null, message1, null, null);
                                smsManager.sendTextMessage(cn.getAddress(), null, message2, null, null);
                            }
                            Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Broadcast Error : " + e.toString());
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        i=0;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mBtAdapter.startDiscovery();
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceName = device.getName();
                        // String deviceAddress = device.getAddress();
                        if (deviceName.equals("OnePlus3"))
                        {
                            Toast.makeText(getApplicationContext(), "Found it!", Toast.LENGTH_LONG).show();
                            SmsManager smsManager = SmsManager.getDefault();
                            KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getApplicationContext());
                            ArrayList<Finding> findings = dbAdapter.getFindings();

                            for (int i = 1; i <= 3; i++) {
                                Finding cn = findings.get(findings.size() - i);
                                smsManager.sendTextMessage(cn.getAddress(), null, message1, null, null);
                                smsManager.sendTextMessage(cn.getAddress(), null, message2, null, null);
                            }
                            Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Broadcast Error : " + e.toString());
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

    }
    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        for(i=0;i<2;i++) {

            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBtAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            mBtAdapter.startDiscovery();
            BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String action = intent.getAction();
                        // When discovery finds a device
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                            // Get the BluetoothDevice object from the Intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            String deviceName = device.getName();
                            // String deviceAddress = device.getAddress();
                            if (deviceName.equals("OnePlus3")) {
                                Toast.makeText(getApplicationContext(), "Found it!", Toast.LENGTH_LONG).show();
                                SmsManager smsManager = SmsManager.getDefault();
                                KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getApplicationContext());
                                ArrayList<Finding> findings = dbAdapter.getFindings();

                                for (int i = 1; i <= 3; i++) {
                                    Finding cn = findings.get(findings.size() - i);
                                    smsManager.sendTextMessage(cn.getAddress(), null, message1, null, null);
                                    smsManager.sendTextMessage(cn.getAddress(), null, message2, null, null);
                                }
                                Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Broadcast Error : " + e.toString());
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            this.registerReceiver(mReceiver, filter);
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            this.registerReceiver(mReceiver, filter);
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(mReceiver, filter);
        }
    }
    @Override
    public void onBackPressed() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_first_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();
        } else if (id == R.id.nav_second_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();
        } else if (id == R.id.nav_third_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ThirdFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}