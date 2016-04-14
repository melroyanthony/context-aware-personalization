package com.example.aero.localife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperActivity extends SQLiteOpenHelper {

    //Variable declarations
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "contextAware";

    private static final String TABLE_PROFILE = "profile";

    private static final String KEY_PROFILE_NAME = "profileName"; //common variable for both the tables
    private static final String KEY_LOCATION_LATITUDE = "locationLatValues"; //variable for profile table
    private static final String KEY_LOCATION_LONGITUDE = "locationLongValues"; //variable for profile table
    private static final String KEY_BLUETOOTH = "bluetoothValue"; //variable for profile-values table

    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + "(" + KEY_PROFILE_NAME + " TEXT, " + KEY_LOCATION_LATITUDE + " TEXT, " + KEY_LOCATION_LONGITUDE + " TEXT, "  + KEY_BLUETOOTH + " TEXT " + ")";

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
        values.put(KEY_BLUETOOTH, profileListActivity.getBluetoothStatus());

        database.insert(TABLE_PROFILE, null, values);
        database.close();
    }

    public List<ProfileListActivity> getAllProfiles(){
        List<ProfileListActivity> profileListActivities = new ArrayList<ProfileListActivity>();

        String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProfileListActivity profileListActivity = new ProfileListActivity();
                profileListActivity.setProfileName(cursor.getString(0));
                profileListActivity.setLatitudeValue(cursor.getString(1));
                profileListActivity.setLongitudeValue(cursor.getString(2));
                profileListActivity.setBluetoothStatus(cursor.getString(3));

                profileListActivities.add(profileListActivity);
            } while (cursor.moveToNext());
        }
        return profileListActivities;
    }

    public void onUpdateBluetoothValue(String profileName, String bluetoothStatus){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BLUETOOTH, bluetoothStatus);

        database.update(TABLE_PROFILE, values, KEY_PROFILE_NAME + "=?", new String[]{String.valueOf(profileName)});
    }

    public String getCurrentBluetoothValue(String profileName) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_PROFILE, new String[] {KEY_PROFILE_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_BLUETOOTH}, KEY_PROFILE_NAME + "=?", new String[]{profileName}, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            String bluetoothValue = cursor.getString(3);
            return bluetoothValue;
        }
        return null;
    }


    public String getProfileForLocationMatched(String serviceLatitudeSubString, String serviceLongitudeSubString) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_PROFILE, new String[]{KEY_PROFILE_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_BLUETOOTH}, KEY_LOCATION_LATITUDE + "=? AND" + KEY_LOCATION_LONGITUDE + "=?", new String[]{serviceLatitudeSubString, serviceLongitudeSubString}, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            String profileName = cursor.getString(0);
            return profileName;
        }
        return null;
    }

}
