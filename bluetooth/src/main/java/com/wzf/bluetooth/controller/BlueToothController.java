package com.wzf.bluetooth.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothController {



    private static class InnerClass {
        private static BlueToothController singletonInstance = new BlueToothController();
    }
    
    private BlueToothController(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    
    public static BlueToothController getInstance(){
            return InnerClass.singletonInstance;
    }

    public BluetoothAdapter getAdapter(){
        return mBluetoothAdapter;
    }

    BluetoothAdapter mBluetoothAdapter;


    public boolean checkBlueToothIsOpen(){
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 打开蓝牙
     * @param activity
     * @param requestCode
     */
    public void openBlueTooth(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
//        if(!checkBlueToothIsOpen()){
//            mBluetoothAdapter.enable();
//        }
    }

    /**
     * 打开蓝牙可见性
     * @param context
     */
    public void enableVisibly(Context context) {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);
    }

    /**
     * 获取绑定设备
     * @return
     */
    public List<BluetoothDevice> getBondedDeviceList() {
        return new ArrayList<>(mBluetoothAdapter.getBondedDevices());
    }

    /**
     * 查找设备
     */
    public void findDevice() {
        assert (mBluetoothAdapter != null);
        mBluetoothAdapter.startDiscovery();
    }


}
