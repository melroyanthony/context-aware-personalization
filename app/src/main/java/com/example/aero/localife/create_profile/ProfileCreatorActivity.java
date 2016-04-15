package com.example.aero.localife.create_profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aero.localife.DatabaseHelperActivity;
import com.example.aero.localife.ProfileAdapterActivity;
import com.example.aero.localife.ProfileListActivity;
import com.example.aero.localife.R;
import com.example.aero.localife.profile_settings.ProfileSettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileCreatorActivity extends AppCompatActivity {

    //Variable Declarations
    FloatingActionButton fabForProfileCreation;
    GPSLocationServiceActivity gpsLocationServiceActivity;
    DatabaseHelperActivity databaseHelperActivity;
    public ProfileAdapterActivity profileAdapterActivity;
    ListView listView;

    //LOG Strings
    private String TAG = "ProfileCreatorActivity";
    private static final String PROFILE_SELECTED_TAG = "Profile Selected LOG:";
    private static final String DIALOG_DECLINED_TAG = "Dialog Declined LOG:";
    public final int LOCATION_PERMISSION = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
           // Log.d(TAG, "Location Permission request required");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        }
        //inflating the layout for profile-creation activity
        setContentView(R.layout.activity_profile_creator);

//        startService(new Intent(getBaseContext(), GPSLocationServiceActivity.class));

        databaseHelperActivity = new DatabaseHelperActivity(getApplicationContext());

        listView = (ListView) findViewById(R.id.listview_profile_creator);

        displayProfiles();

        //Code to handle the floating action button to create profile
        fabForProfileCreation = (FloatingActionButton) findViewById(R.id.fab_profile_creator);
        fabForProfileCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = new LinearLayout(ProfileCreatorActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(ProfileCreatorActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.profile_creator_dialog_title);

                //Code for EditText for ProfileName
                final EditText editTextProfileName = new EditText(ProfileCreatorActivity.this);
                editTextProfileName.setHint(R.string.profile_creator_dialog_message);
                editTextProfileName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                layout.addView(editTextProfileName);

                //Code to display TextView for Latitude
                final TextView textViewLatitude = new TextView(ProfileCreatorActivity.this);
                textViewLatitude.setGravity(Gravity.CENTER);
                textViewLatitude.setTextSize(16);
                textViewLatitude.setPadding(0, 10, 0, 10);
                layout.addView(textViewLatitude);

                //Code to display TextView for Longitude
                final TextView textViewLongitude = new TextView(ProfileCreatorActivity.this);
                textViewLongitude.setGravity(Gravity.CENTER);
                textViewLongitude.setTextSize(16);
                textViewLongitude.setPadding(0, 0, 0, 10);
                layout.addView(textViewLongitude);

                final CheckBox checkBoxCurrentLocation = new CheckBox(ProfileCreatorActivity.this);
                checkBoxCurrentLocation.setChecked(false);
                checkBoxCurrentLocation.setText("Get current location");
                checkBoxCurrentLocation.setTextColor(Color.rgb(0, 0, 0));
                layout.addView(checkBoxCurrentLocation);

                //Code for Button that triggers the GPS Location Service to fetch location co-ordinates
                Button buttonLForCurrentLocation = new Button(ProfileCreatorActivity.this);
                buttonLForCurrentLocation.setText("CURRENT LOCATION");
                buttonLForCurrentLocation.setGravity(Gravity.CENTER);
                buttonLForCurrentLocation.setBackgroundColor(Color.rgb(0, 150, 136));
                buttonLForCurrentLocation.setTextColor(Color.rgb(255, 255, 255));
                buttonLForCurrentLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "in inside");
                        gpsLocationServiceActivity = new GPSLocationServiceActivity(ProfileCreatorActivity.this);

                        if (gpsLocationServiceActivity.canGetLocation()) {

                            //code to fetch the latitude & longitude for the current profile
                            String latitude = String.valueOf(gpsLocationServiceActivity.getLatitude());
                            String longitude = String.valueOf(gpsLocationServiceActivity.getLongitude());
                        //    textViewLatitude.setText(latitude.substring(0, 7)
                            //    textViewLongitude.setText(longitude.substring(0, 7));
                            textViewLatitude.setText(latitude);
                            textViewLongitude.setText(longitude);


                        } else {

                            gpsLocationServiceActivity.showSettingsAlert();

                        }
                    }
                });
                layout.addView(buttonLForCurrentLocation);

                builder.setView(layout, 40, 20, 40, 24);

                builder.setPositiveButton(R.string.profile_creator_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextProfileName.getText().toString().trim().isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Cannot create blank profile!", Toast.LENGTH_SHORT).show();

                        } else if (textViewLatitude.getText().toString().trim().isEmpty() && textViewLongitude.getText().toString().trim().isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Cannot Proceed without a location. Click on CURRENT LOCATION button!", Toast.LENGTH_LONG).show();

                        } else {

                            String profileName =  editTextProfileName.getText().toString().trim();
                            String latitudeValue = textViewLatitude.getText().toString().trim();
                            String longitudeValue = textViewLongitude.getText().toString().trim();
                            String statusValue = "OFF";
                            databaseHelperActivity.createNewProfile(new ProfileListActivity(profileName, latitudeValue, longitudeValue, statusValue));
                            displayProfiles();
                            dialog.dismiss();

                        }
                    }
                });

                builder.setNegativeButton(R.string.profile_creator_dialog_decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i(DIALOG_DECLINED_TAG, "Declined!");
                        dialog.cancel();

                    }
                });

                builder.show();

            }
        });

        registerForContextMenu(listView);

    }

    private void displayProfiles() {
        List<ProfileListActivity> profileListItems = new ArrayList<ProfileListActivity>();
        profileListItems.clear();
        profileListItems = databaseHelperActivity.getAllProfiles();
        ProfileAdapterActivity profileAdapter = new ProfileAdapterActivity(ProfileCreatorActivity.this, (ArrayList<ProfileListActivity>) profileListItems);
        for (ProfileListActivity pl : profileListItems){
            String log = "Profile Name: " + pl.getProfileName() + " Latitude: " + pl.getLatitudeValue() + " Longitude: " + pl.getLongitudeValue() + " Bluetooth Status: " + pl.getBluetoothStatus();
            Log.d("Profile created: ", log);
        }
        listView.setAdapter(profileAdapter);
        final List<ProfileListActivity> finalProfileListItems = profileListItems;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Code to handle the item click events for listview
                String selectedProfile = finalProfileListItems.get(position).getProfileName();
                Log.i(PROFILE_SELECTED_TAG, "Opening "+selectedProfile);
                gotoProfileSettingsActivity(selectedProfile);

            }
        });

        registerForContextMenu(listView);
    }

    private void gotoProfileSettingsActivity(String selectedProfile) {

        Intent intent = new Intent(ProfileCreatorActivity.this, ProfileSettingsActivity.class);
        intent.putExtra("Profile Selected", selectedProfile);
        startActivity(intent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //TODO logic for creating a custom context menu to delete the profile
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayProfiles();
    }
}


