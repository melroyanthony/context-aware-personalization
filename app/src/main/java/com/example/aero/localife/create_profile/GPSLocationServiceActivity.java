package com.example.aero.localife.create_profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.aero.localife.DatabaseHelperActivity;
import com.example.aero.localife.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class GPSLocationServiceActivity extends Service implements LoaderManager.LoaderCallbacks<Cursor>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    public static int LOCATION_PERMISSION = 111;
    private final int SERVICE_ID = 101;
    public GoogleApiClient mGoogleApiClient;
    private String TAG = "GPSLocationServiceActivity";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private Location mLastLocation;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSLocationServiceActivity(Context context) {
        this.mContext = context;
        getLocation();
    }
    Notification notification = null;

    public GPSLocationServiceActivity() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
      // location = getLocation();
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }
        DatabaseHelperActivity databaseHelperActivity = new DatabaseHelperActivity(GPSLocationServiceActivity.this);
        if(location != null) {
            String serviceLatitude = String.valueOf(location.getLatitude());
            String serviceLatitudeSubString = serviceLatitude.substring(0, 7);

            String serviceLongitude = String.valueOf(location.getLongitude());
            String serviceLongitudeSubString = serviceLongitude.substring(0, 7);

            String matchedProfile = databaseHelperActivity.getProfileForLocationMatched(serviceLatitudeSubString, serviceLongitudeSubString);
            String bluetoothStatus = databaseHelperActivity.getCurrentBluetoothValue(matchedProfile);

            String bluetoothON = "ON";

            if (bluetoothStatus.equals(bluetoothON.trim())) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.enable();
                Toast.makeText(GPSLocationServiceActivity.this, matchedProfile + " is Activated!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GPSLocationServiceActivity.this, matchedProfile + " is not active!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
        String latitude = String.valueOf(getLatitude());
        String longitude = String.valueOf(getLongitude());

        createLocationRequest();

        return START_STICKY;
    }

    public Location getLocation() {
        /*
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location; */
        return null;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    protected void createLocationRequest() {

        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());


        Log.d(TAG, "creating request");

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResult(LocationSettingsResult result) {
                Log.d(TAG, "in on result");

                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        //...
                        Log.d(TAG, "in Sucess");

                        try {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            if (mLastLocation == null)
                                Log.d(TAG, "mLastLocation is null");
                            if (mGoogleApiClient == null)
                                Log.d(TAG, "mGoogleAPiCLient is null");
                            else
                                Log.d(TAG, "nothing is null");


                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                        if (mLastLocation != null) {
                            Toast.makeText(getApplicationContext(), "Latitude: " + String.valueOf(mLastLocation.getLatitude()) + ", " + "Longitude: " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Latitude: " + String.valueOf(mLastLocation.getLatitude()) + ", " + "Longitude: " + String.valueOf(mLastLocation.getLongitude()));
                            double latitude = mLastLocation.getLatitude();
                            double longitude = mLastLocation.getLongitude();
                            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Location Service Running")
                                    .setContentText("Latitude: "+latitude+", Longitude: "+longitude);
                            notification = builder.build();
                            startForeground(SERVICE_ID, notification);

                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d(TAG, "in on resolution required");


                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "in on no option");

                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
