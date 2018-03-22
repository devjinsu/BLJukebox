package com.jinsukim.bljukebox.data;

import android.provider.BaseColumns;

/**
 * Created by JinsuKim on 2018. 2. 19..
 */

public class BlDeviceInfo {
    private String mUUID;
    private String mName;
    private boolean IsCall;
    private int mCallVolume;
    private boolean IsMusic;
    private int mMusicVolume;
    private String mApp;

    /* Inner class that defines the table contents */
    public static class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "Device";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_UUID = "UUID";
        public static final String COLUMN_NAME_CALL_ENABLE = "CALL_ENABLE";
        public static final String COLUMN_NAME_CALL_VOLUME = "CALL_VOLUME";
        public static final String COLUMN_NAME_MUSIC_ENABLE = "MUSIC_ENABLE";
        public static final String COLUMN_NAME_MUSIC_VOLUME = "MUSIC_VOLUME";
        public static final String COLUMN_NAME_LAUNCH_APP = "LAUNCH_APP";
    }

    public BlDeviceInfo(String UUID, String name, boolean isCall, int callVolume, boolean isMusic, int musicVolume, String app) {
        mUUID = UUID;
        mName = name;
        IsCall = isCall;
        mCallVolume = callVolume;
        IsMusic = isMusic;
        mMusicVolume = musicVolume;
        mApp = app;
    }

    public String getUUID() {
        return mUUID;
    }

    public String getName() {
        return mName;
    }

    public boolean isCall() {
        return IsCall;
    }

    public int getCallVolume() {
        return mCallVolume;
    }

    public boolean isMusic() {
        return IsMusic;
    }

    public int getMusicVolume() {
        return mMusicVolume;
    }

    public String getApp() {
        return mApp;
    }
}
