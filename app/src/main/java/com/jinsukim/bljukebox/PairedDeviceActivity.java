package com.jinsukim.bljukebox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PairedDeviceActivity extends AppCompatActivity {
    public static final String RESULT_SELECTED_DEVICE = "RESULT_SELECTED_DEVICE";
    private ListView mLVPairedDevice;
    private ArrayAdapter<String> mDeviceAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private List<String> mDeviceNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_device);
        setTitle(R.string.add_new_device);
        mLVPairedDevice = (ListView) findViewById(R.id.lv_paired_device);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDeviceNames = new ArrayList<String>();

        mDeviceAdapter = new ArrayAdapter<String>(this, R.layout.lv_paired_device, R.id.tv_paired_device);
        mLVPairedDevice.setAdapter(mDeviceAdapter);

        mLVPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent result = new Intent();
                String selectedDevice = mDeviceNames.get(i);
                for(BluetoothDevice bt : mPairedDevices){
                    if (bt.getName().equals(selectedDevice)){
                        result.putExtra(RESULT_SELECTED_DEVICE, bt);
                        setResult(RESULT_OK, result);
                    }
                }
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPairedDevices = mBluetoothAdapter.getBondedDevices();

        for(BluetoothDevice bt : mPairedDevices)
            mDeviceNames.add(bt.getName());


        if(mDeviceNames.size() == 0){
            (new AlertDialog.Builder(this)).setTitle(R.string.alert)
                    .setMessage(R.string.no_paired_device_alert)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentOpenBluetoothSettings = new Intent();
                            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intentOpenBluetoothSettings);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    }).create().show();
        }else{
            mDeviceAdapter.clear();
            mDeviceAdapter.addAll(mDeviceNames);
            mDeviceAdapter.notifyDataSetChanged();
        }
    }
}
