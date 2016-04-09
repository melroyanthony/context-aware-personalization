package com.example.aero.localife.create_profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aero.localife.DatabaseHelperActivity;
import com.example.aero.localife.ProfileListActivity;
import com.example.aero.localife.R;
import com.example.aero.localife.profile_settings.ProfileSettingsActivity;

public class ProfileCreatorActivity extends AppCompatActivity {

    //Variable Declarations
    FloatingActionButton fabForProfileCreation;
    GPSLocationServiceActivity gpsLocationServiceActivity;
    DatabaseHelperActivity databaseHelperActivity;

    //LOG Strings
    private static final String PROFILE_SELECTED_TAG = "Profile Selected LOG:";
    private static final String DIALOG_DECLINED_TAG = "Dialog Declined LOG:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflating the layout for profile-creation activity
        setContentView(R.layout.activity_profile_creator);

        databaseHelperActivity = new DatabaseHelperActivity(getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.listview_profile_creator);

        //TODO transfer this logic to display the list-item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Code to handle the item click events for listview
//                String selectedProfile = profileListActivity.get(position).getProfileName();
//                Log.i(PROFILE_SELECTED_TAG, "Opening "+selectedProfile);
//                gotoProfileSettingsActivity(selectedProfile);

            }
        });

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

                //Code for Button that triggers the GPS Location Service to fetch location co-ordinates
                Button buttonLForCurrentLocation = new Button(ProfileCreatorActivity.this);
                buttonLForCurrentLocation.setText("CURRENT LOCATION");
                buttonLForCurrentLocation.setGravity(Gravity.CENTER);
                buttonLForCurrentLocation.setBackgroundColor(Color.rgb(0, 150, 136));
                buttonLForCurrentLocation.setTextColor(Color.rgb(255, 255, 255));
                buttonLForCurrentLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        gpsLocationServiceActivity = new GPSLocationServiceActivity(ProfileCreatorActivity.this);

                        if (gpsLocationServiceActivity.canGetLocation()) {

                            //code to fetch the latitude & longitude for the current profile
                            double latitude = gpsLocationServiceActivity.getLatitude();
                            double longitude = gpsLocationServiceActivity.getLongitude();
                            textViewLatitude.setText("Latitude: " + latitude);
                            textViewLongitude.setText("Longitude: " + longitude);

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
                            //TODO logic for entering values in profile table
                            databaseHelperActivity.createNewProfile(new ProfileListActivity(profileName, latitudeValue, longitudeValue));
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

    private void gotoProfileSettingsActivity(String selectedProfile) {

        Intent intent = new Intent(ProfileCreatorActivity.this, ProfileSettingsActivity.class);
        intent.putExtra("Profile Selected", selectedProfile);
        startActivity(intent);

    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//        // We know that each row in the adapter is a Map
//        ProfileListActivity profileListActivity =  profileAdapterActivity.getItem(aInfo.position);
//
//        menu.setHeaderTitle("Options for " + profileListActivity.getProfileName());
//        menu.add(1, 1, 1, "Delete");
//
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        profileListActivity.remove(aInfo.position);
//        profileAdapterActivity.notifyDataSetChanged();
//        return true;
//    }

}


