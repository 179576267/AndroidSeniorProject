package com.wzf.bluetooth.model;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public class BluetoothDeviceInfo implements Serializable {
    private BluetoothDevice device;
    private int rssi;

    public BluetoothDeviceInfo(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public int getRssi() {
        return rssi;
    }
}
