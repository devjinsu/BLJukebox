package com.jinsukim.bljukebox.data;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinsuKim on 2018. 2. 19..
 */

public class DeviceDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BlDeviceInfo.DeviceEntry.TABLE_NAME + " (" +
                    BlDeviceInfo.DeviceEntry._ID + " INTEGER PRIMARY KEY," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_NAME + " TEXT," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_UUID + " TEXT," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_ENABLE + " INTEGER," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_VOLUME + " INTEGER," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_ENABLE + " INTEGER," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_VOLUME + " INTEGER," +
                    BlDeviceInfo.DeviceEntry.COLUMN_NAME_LAUNCH_APP + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BlDeviceInfo.DeviceEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BLDevice.db";

    public DeviceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void InsertNewDevice(BluetoothDevice device, int volume){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_NAME, device.getName());
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_UUID, device.getUuids().toString());
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_ENABLE, 0);
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_VOLUME, 0);
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_ENABLE, 1);
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_VOLUME, volume);
        values.put(BlDeviceInfo.DeviceEntry.COLUMN_NAME_LAUNCH_APP, "N/A");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(BlDeviceInfo.DeviceEntry.TABLE_NAME, null, values);
    }

    public BlDeviceInfo GetDeviceByUUID(String UUID){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BlDeviceInfo.DeviceEntry._ID,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_NAME,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_ENABLE,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_VOLUME,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_ENABLE,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_VOLUME,
                BlDeviceInfo.DeviceEntry.COLUMN_NAME_LAUNCH_APP
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = BlDeviceInfo.DeviceEntry.COLUMN_NAME_UUID + " = ?";
        String[] selectionArgs = { UUID };

         // How you want the results sorted in the resulting Cursor
        String sortOrder =
                BlDeviceInfo.DeviceEntry._ID + " DESC";

        Cursor cursor = db.query(
                BlDeviceInfo.DeviceEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<BlDeviceInfo> itemIds = new ArrayList<BlDeviceInfo>();
        while(cursor.moveToNext()) {
            String unique = cursor.getString(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_UUID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_NAME));
            int callEnable = cursor.getInt(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_ENABLE));
            int callVolume = cursor.getInt(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_CALL_VOLUME));
            int musicEnable = cursor.getInt(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_ENABLE));
            int musicVolume = cursor.getInt(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_MUSIC_VOLUME));
            String app = cursor.getString(
                    cursor.getColumnIndexOrThrow(BlDeviceInfo.DeviceEntry.COLUMN_NAME_LAUNCH_APP));

            itemIds.add(new BlDeviceInfo(unique, name, callEnable == 1 ? true : false, callVolume,
                    musicEnable == 1 ? true : false, musicVolume, app));
        }
        cursor.close();

        switch (itemIds.size()){
            case 0:
                return null;
            default:
                if (itemIds.size() > 1){
                    Log.w(this.toString(), "Select UUID more than 1 result. Something gonna wrong");
                }
                return itemIds.get(0);

        }
    }
}
