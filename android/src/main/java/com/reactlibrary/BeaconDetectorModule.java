package com.reactlibrary;

import android.content.Context;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.minew.beacon.BeaconValueIndex;

import java.util.List;

public class BeaconDetectorModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Context mApplicationContext;
    private MinewBeaconManager mMinewBeaconManager;

    public BeaconDetectorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "BeaconDetector";
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        this.reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }

    @ReactMethod
    public void init() {
        this.mApplicationContext = this.reactContext.getApplicationContext();
        this.mMinewBeaconManager = MinewBeaconManager.getInstance(mApplicationContext);
        this.mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {

           // if the manager find some new beacon, it will call back this method.
           // @param minewBeacons  new beacons the manager scanned
           @Override
           public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

           }

           // if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
           // @param minewBeacons beacons out of range
           @Override
           public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
               /*for (MinewBeacon minewBeacon : minewBeacons) {
                   String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                   Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
               }*/
           }

           // the manager calls back this method every 1 seconds, you can get all scanned beacons.
           // @param minewBeacons all scanned beacons
            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                // runOnUiThread(new Runnable() {
                //     @Override
                //     public void run() {
                //            Collections.sort(minewBeacons, comp);
                //            Log.e("tag", state + "");
                //            if (state == 1 || state == 2) {
                //            } else {
                //                mAdapter.setItems(minewBeacons);
                //            }
                //     }
                // });
                WritableMap params = Arguments.createMap();
                for (int i=0; i<minewBeacons.size(); i++) {
                    params.putString("beacon" + i, minewBeacons.get(i).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_UUID).getStringValue());
                }
                sendEvent("onRangeBeacons", params);
            }

            // the manager calls back this method when BluetoothStateChanged.
            // @param state BluetoothState
            @Override
            public void onUpdateState(BluetoothState state) {
                // switch (state) {
                //     case BluetoothStatePowerOn:
                //         Toast.makeText(getApplicationContext(), "BluetoothStatePowerOn", Toast.LENGTH_SHORT).show();
                //         break;
                //     case BluetoothStatePowerOff:
                //         Toast.makeText(getApplicationContext(), "BluetoothStatePowerOff", Toast.LENGTH_SHORT).show();
                //         break;
                // }
            }
        });
    }

    // @ReactMethod
    // public void checkBluetooth(Callback callback) {
    //     BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
    //     switch (bluetoothState) {
    //         case BluetoothStateNotSupported:
    //             Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
    //             finish();
    //             break;
    //         case BluetoothStatePowerOff:
    //             showBLEDialog();
    //             break;
    //         case BluetoothStatePowerOn:
    //             break;
    //     }
    //     callback.invoke()
    // }

    @ReactMethod
    public void scan() {
        this.mMinewBeaconManager.startScan();
    }
}