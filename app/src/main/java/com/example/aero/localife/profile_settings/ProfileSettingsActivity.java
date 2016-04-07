package com.example.aero.localife.profile_settings;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.aero.localife.R;

public class ProfileSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private AppCompatDelegate appCompatDelegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile_settings);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String activityTitle = getIntent().getStringExtra("Profile Selected");
        toolbar.setTitle(activityTitle);
        addPreferencesFromResource(R.xml.pref_settings);

        PreferenceManager.setDefaultValues(ProfileSettingsActivity.this, R.xml.pref_settings, false);
        initSummary(getPreferenceScreen());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {

        if (p instanceof RingtonePreference) {
            final RingtonePreference ringtonePreference = (RingtonePreference) p;
            p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Ringtone ringtone = RingtoneManager.getRingtone(ProfileSettingsActivity.this, Uri.parse((String) newValue));
                    ringtonePreference.setSummary(ringtone.getTitle(ProfileSettingsActivity.this));
                    return true;
                }
            });
            String ringtonePath = p.getSharedPreferences().getString(p.getKey(), "ringtone_pref_selection");
            Ringtone ringtone = RingtoneManager.getRingtone(ProfileSettingsActivity.this, Uri.parse(ringtonePath));
            ringtonePreference.setSummary(ringtone.getTitle(ProfileSettingsActivity.this));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (appCompatDelegate == null) {
            appCompatDelegate = AppCompatDelegate.create(this, null);
        }
        return appCompatDelegate;
    }
}
