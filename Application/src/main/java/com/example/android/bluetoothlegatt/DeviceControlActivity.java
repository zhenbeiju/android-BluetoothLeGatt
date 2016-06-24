/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import commanutil.utl.LogManager;
import commanutil.utl.net.NetDataTypeTransform;
import commanutil.view.DialogInfo;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    @butterknife.Bind(R.id.device_address)
    TextView deviceAddress;
    @butterknife.Bind(R.id.connection_state)
    TextView connectionState;
    @butterknife.Bind(R.id.send_data_value)
    TextView sendDataValue;
    @butterknife.Bind(R.id.data_value)
    TextView dataValue;
    @butterknife.Bind(R.id.send_verify)
    Button sendVerify;
    @butterknife.Bind(R.id.send_read_alarm_sleep)
    Button sendReadAlarmSleep;
    @butterknife.Bind(R.id.send_read_alarm)
    Button sendReadAlarm;
    @butterknife.Bind(R.id.send_read_clock)
    Button sendReadClock;
    @butterknife.Bind(R.id.send_set_sound)
    Button sendSetSound;
    @butterknife.Bind(R.id.set_sound)
    LinearLayout setSound;
    @butterknife.Bind(R.id.send_set_clock)
    Button sendSetClock;
    @butterknife.Bind(R.id.set_clock)
    LinearLayout setClock;
    @butterknife.Bind(R.id.send_set_alarm)
    Button sendSetAlarm;
    @butterknife.Bind(R.id.set_alarm)
    LinearLayout setAlarm;
    @butterknife.Bind(R.id.send_set_time)
    Button sendSetTime;
    @butterknife.Bind(R.id.send_sleep_time)
    Button sendSleepTime;

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    BluetoothGattCharacteristic mSendCharacteristic;
    private boolean mConnected = false;
    private boolean needSendVerify = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
                needSendVerify = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//                if (needSendVerify) {
//                    needSendVerify = false;
//                    sendVerify();
//                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                LogManager.e(intent.getExtras().toString());
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            } else if (BluetoothLeService.SEND_EXTRA_DATA.equals(action)) {
                sendDataValue.setText(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };


    private void clearUI() {
        dataValue.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        butterknife.ButterKnife.bind(this);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
//        mGattServicesList.setOnChildClickListener(servicesListClickListner);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }


    @OnClick(R.id.send_read_alarm)
    public void sendReadAlarm() {
        String command = "0a";
        sendCommand(command);

    }


    @OnClick(R.id.send_read_alarm_sleep)
    public void sendReadAlarm_sleep() {
        String command = "09";
        sendCommand(command);


    }

    @OnClick(R.id.send_read_clock)
    public void sendReadClock() {
        String command = "0b";
        sendCommand(command);

    }

    @OnClick(R.id.send_set_clock)
    public void sendSetClock() {
        String command = "0d000222220508";
        sendCommand(command);

    }

    @OnClick(R.id.send_set_sound)
    public void sendSetSound() {
        String command = "0c000601";
        sendCommand(command);

    }

    @OnClick(R.id.send_set_time)
    public void sendSetTime() {
        String command = "0f0001";
        sendCommand(command);
    }

    @OnClick(R.id.send_set_alarm)
    public void sendSetAlarm() {
        String command = "0e00010122057f08";
        sendCommand(command);
    }


    @OnClick(R.id.send_sleep_time)
    public void sendSetSleep() {
        String command = "10000202";
        sendCommand(command);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            dataValue.setText(data);
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
//        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
//                = new ArrayList<ArrayList<HashMap<String, String>>>();
//        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.equals("0000ee02-0000-1000-8000-00805f9b34fb")) {
                    mSendCharacteristic = gattCharacteristic;
                }
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
//            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

//        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
//                this,
//                gattServiceData,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[]{LIST_NAME, LIST_UUID},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                gattCharacteristicData,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[]{LIST_NAME, LIST_UUID},
//                new int[]{android.R.id.text1, android.R.id.text2}
//        );
//        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    public void sendVerify() {
        String command = "0800" + NetDataTypeTransform.BytesToIntString("001PC TDI".getBytes());
        sendCommand(command);
    }

    public void sendCommand(String command) {
        byte[] byteCommand = NetDataTypeTransform.intStringToByte(command);
        if (mSendCharacteristic != null) {
            LogManager.e(mSendCharacteristic.getUuid().toString());
            if (mSendCharacteristic.getUuid().toString().equals("")) {
                mBluetoothLeService.sendData(mSendCharacteristic, byteCommand);
                LogManager.e("send data : " + mSendCharacteristic.getUuid() + "|" + command);
            }
        } else {
            DialogInfo.showToast("not connect yet");
//            LogManager.e("not connect");
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.SEND_EXTRA_DATA);
        return intentFilter;
    }
}
