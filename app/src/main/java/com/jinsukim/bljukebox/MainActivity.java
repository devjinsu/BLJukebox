package com.jinsukim.bljukebox;

import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.jinsukim.bljukebox.data.DeviceDbHelper;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_DEVICE = 1001;
    private DeviceDbHelper mDbHelper;
    private int mUserVolumeLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                getWindow().setExitTransition(new Explode());
                Intent intent = new Intent(MainActivity.this, PairedDeviceActivity.class);
                startActivityForResult(intent, REQUEST_DEVICE,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()

                );
            }
        });

        mDbHelper = new DeviceDbHelper(this);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(new BluetoothConnectionReceiver(), filter3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        mUserVolumeLevel= am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(this.toString(), "volume lev: " + mUserVolumeLevel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_DEVICE:
                if (resultCode == RESULT_OK){
                    BluetoothDevice device = data.getParcelableExtra(PairedDeviceActivity.RESULT_SELECTED_DEVICE);
                    Log.i(this.toString(), "Selected device: " + device.toString());
                    mDbHelper.InsertNewDevice(device, mUserVolumeLevel);
                }
                break;
            default:
                break;
        }
    }
}
