package com.example.aero.localife;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperActivity extends SQLiteOpenHelper {

    //Variable declarations
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "contextAware";

    private static final String TABLE_PROFILE = "profile";

    private static final String KEY_ID = "id"; //common variable for both the tables
    private static final String KEY_PROFILE_NAME = "profileName"; //common variable for both the tables
    private static final String KEY_LOCATION_LATITUDE = "locationLatValues"; //variable for profile table
    private static final String KEY_LOCATION_LONGITUDE = "locationLongValues"; //variable for profile table
    private static final String KEY_STATUS = "statusValue"; //variable for profile-values table
    private static final String KEY_RINGTONE = "ringtoneValue"; //variable for profile-values table
    private static final String KEY_NOTIFICATION = "notificationValue"; //variable for profile-values table

    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_PROFILE_NAME + " TEXT, " + KEY_LOCATION_LATITUDE + " TEXT, " + KEY_LOCATION_LONGITUDE + " TEXT " + KEY_STATUS + " BOOLEAN, " + KEY_RINGTONE + " TEXT, " + KEY_NOTIFICATION + " TEXT" + ")";

    //LOG Strings
    private static final String LOG = DatabaseHelperActivity.class.getName();

     public DatabaseHelperActivity (Context context) {

         super(context, DATABASE_NAME, null, DATABASE_VERSION);

     }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PROFILE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);

        onCreate(db);

    }

    //Methods for Profile Table
    public void createNewProfile(ProfileListActivity profileListActivity){

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_NAME, profileListActivity.getProfileName());
        values.put(KEY_LOCATION_LATITUDE, profileListActivity.getLatitudeValue());
        values.put(KEY_LOCATION_LONGITUDE, profileListActivity.getLongitudeValue());

        database.insert(TABLE_PROFILE, null, values);
        database.close();
    }

}
