package com.jinsukim.bljukebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by JinsuKim on 2018. 2. 26..
 */

public class BluetoothConnectionReceiver extends BroadcastReceiver {
    public BluetoothConnectionReceiver(){
        //No initialisation code needed
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch(action){
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(this.toString(),
                        "ACTION_ACL_CONNECTED: " + device.toString());
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                Log.i(this.toString(),
                        "ACTION_ACL_DISCONNECTED");
                break;
        }
    }
}
