package com.example.aero.localife.profile_settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.aero.localife.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbarProfileSettings = (Toolbar) findViewById(R.id.toolbar_profile_settings);
        setSupportActionBar(toolbarProfileSettings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String activityTitle = getIntent().getStringExtra("Profile Selected");
        setTitle(activityTitle);
    }

    //This method handles the Up-Navigation
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
