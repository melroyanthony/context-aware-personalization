package com.example.aero.localife.profile_settings;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.aero.localife.DatabaseHelperActivity;
import com.example.aero.localife.R;

public class ProfileSettingsActivity extends PreferenceActivity {

    DatabaseHelperActivity databaseHelperActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String activityTitle = getIntent().getStringExtra("Profile Selected");
        toolbar.setTitle(activityTitle);
        addPreferencesFromResource(R.xml.pref_settings);

        //Defining the Preferences
        final SwitchPreference bluetoothStatus = (SwitchPreference) findPreference("switch_pref_enable_disable_profile");

        final String profileDisabled = bluetoothStatus.getSwitchTextOff().toString().trim();
        final String profileEnabled = bluetoothStatus.getSwitchTextOn().toString().trim();

        //TODO logic for inflating the values for the bluetooth toggle for the corresponding profile

        databaseHelperActivity = new DatabaseHelperActivity(ProfileSettingsActivity.this);
        final String returnedBluetoothValue = databaseHelperActivity.getCurrentBluetoothValue(activityTitle);
        Log.i("Bluetooth Value LOG:", returnedBluetoothValue);

        if (returnedBluetoothValue.equals(profileEnabled.trim())){
            bluetoothStatus.setChecked(true);
//            bluetoothStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    bluetoothStatus.setSummaryOff(R.string.switch_pref_summary_off);
//                    databaseHelperActivity.onUpdateBluetoothValue(activityTitle, profileDisabled);
//                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    mBluetoothAdapter.disable();
//                    return true;
//                }
//            });

            bluetoothStatus.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    bluetoothStatus.setSummaryOff(R.string.switch_pref_summary_off);
                    databaseHelperActivity.onUpdateBluetoothValue(activityTitle, profileDisabled);
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothAdapter.disable();
                    return true;
                }
            });
        }
        else {
            bluetoothStatus.setChecked(false);
//            bluetoothStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    bluetoothStatus.setSummaryOn(R.string.switch_pref_summary_on);
//                    databaseHelperActivity.onUpdateBluetoothValue(activityTitle, profileEnabled);
//                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    mBluetoothAdapter.enable();
//                    return true;
//                }
//            });

            bluetoothStatus.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    bluetoothStatus.setSummaryOn(R.string.switch_pref_summary_on);
                    databaseHelperActivity.onUpdateBluetoothValue(activityTitle, profileEnabled);
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothAdapter.enable();
                    return true;
                }
            });
        }
    }

}
