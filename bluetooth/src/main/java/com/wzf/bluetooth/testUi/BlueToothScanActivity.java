package com.wzf.bluetooth.testUi;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.base.rx.PermissionConsumer;
import com.demo.base.ui.adapter.RcyCommonAdapter;
import com.demo.base.ui.adapter.RcyViewHolder;
import com.hjq.toast.ToastUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wzf.base.ui.activity.BaseMenuActivity;
import com.wzf.base.ui.base.BaseActivity;
import com.wzf.base.ui.dialog.AlertCommonDialog;
import com.wzf.bluetooth.R;
import com.wzf.bluetooth.connect.Constant;
import com.wzf.bluetooth.controller.BlueToothController;
import com.wzf.bluetooth.controller.ChatController;
import com.wzf.bluetooth.model.BluetoothDeviceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothScanActivity extends BaseMenuActivity {
    private static final int REQUEST_CODE = 12545;
    private RecyclerView rv;
    private TextView tv_content;
    private RcyCommonAdapter<BluetoothDeviceInfo> adapter;
    private StringBuilder mChatText = new StringBuilder();
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MSG_START_LISTENING:
                    setProgressBarIndeterminateVisibility(true);
                    clearRcy();
                    break;
                case Constant.MSG_FINISH_LISTENING:
                    setProgressBarIndeterminateVisibility(false);
                    clearRcy();
                    break;
                case Constant.MSG_GOT_DATA:
                    byte[] data = (byte[]) msg.obj;
                    mChatText.append(ChatController.getInstance().decodeMessage(data)).append("\n");
                    tv_content.setText(mChatText.toString());
                    break;
                case Constant.MSG_ERROR:
                    clearRcy();
                    showToast("error: "+String.valueOf(msg.obj));
                    break;
                case Constant.MSG_CONNECTED_TO_SERVER:
                    clearRcy();
                    showToast("Connected to Server");
                    break;
                case Constant.MSG_GOT_A_CLINET:
                    clearRcy();
                    showToast("Got a Client");
                    break;
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){ // 开始查找
                showDialog("正在查找设备");
                clearRcy();
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){ // 查找完成
                dissmissDialog();
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){ // 发现一个设备
                // 从 Intent 中获取发现的 BluetoothDevice
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
                        Short.MIN_VALUE);
                // 将名字和地址放入要显示的适配器中
                if(!TextUtils.isEmpty(device.getName())){
                    adapter.addItem(new BluetoothDeviceInfo(device, rssi));
                }
            }else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){ //设备蓝牙可见性改变
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,0);
                if( scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) { // 可见
                    showToast("蓝牙可见");
                }
                else {
                    showToast("蓝牙不可见");
                }
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){ //绑定状态改变
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if( remoteDevice == null ) {
                    showToast("no device");
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0);
                if( status == BluetoothDevice.BOND_BONDED) {
                    showToast("已绑定 " + remoteDevice.getName());
                }
                else if( status == BluetoothDevice.BOND_BONDING){
                    showToast("绑定中 " + remoteDevice.getName());
                }
                else if(status == BluetoothDevice.BOND_NONE){
                    showToast("解除绑定 " + remoteDevice.getName());
                }
            }

        }
    };

    @Override
    public int getContainerLayoutResId() {
        return R.layout.activity_bluetoothscan;
    }


    @Override
    protected void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        rv = findViewById(R.id.rv);
        tv_content = findViewById(R.id.tv_content);

        registerBluetoothReceiver();
        initView();

        addChild("获取绑定设备", (v)->{getBondedDeviceList(); });
        addChild("查找设备", (v)->{findDevice(); });
        addChild("等待监听", (v)->{ ChatController.getInstance().waitingForFriends(BlueToothController.getInstance().getAdapter(), mHandler); });

        addChild("停止监听", (v)->{ChatController.getInstance().stopChat();clearRcy();});
        addChild("发送消息", (v)->{
            String ext = System.currentTimeMillis() + "";
            ChatController.getInstance().sendMessage(ext);
            mChatText.append(ext).append("\n");
            tv_content.setText(mChatText.toString());});
    }

    private void initView() {
        rv.setLayoutManager(new LinearLayoutManager(bActivity));
        rv.setAdapter(adapter = new RcyCommonAdapter<BluetoothDeviceInfo>(bActivity, new ArrayList<>(), false, rv) {
            @Override
            public int getLayoutId(int position) {
                return R.layout.item_bluetooth;
            }

            @Override
            public void convert(RcyViewHolder holder,  BluetoothDeviceInfo device, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_mac = holder.getView(R.id.tv_mac);
                TextView tv_single = holder.getView(R.id.tv_single);
                TextView tv_connect = holder.getView(R.id.tv_connect);
                tv_name.setText("Name: " + device.getDevice().getName());
                tv_mac.setText("Mac: " + device.getDevice().getAddress());
                tv_single.setText("Rssi: " + device.getRssi());
                int status = device.getDevice().getBondState();

                if( status == BluetoothDevice.BOND_BONDED) {
                    tv_connect.setText("已绑定");
                    tv_connect.setOnClickListener((v)->{
                        ChatController.getInstance().startChatWith(device.getDevice(), BlueToothController.getInstance().getAdapter(),mHandler);
                    });

                }
                else if( status == BluetoothDevice.BOND_BONDING){
                    tv_connect.setText("绑定中");
                    tv_connect.setOnClickListener((v)->{});
                }
                else if(status == BluetoothDevice.BOND_NONE){
                    tv_connect.setText("未绑定");
                    tv_connect.setOnClickListener((v)->{
                        device.getDevice().createBond();
                    });
                }
            }
        });
    }

    private void registerBluetoothReceiver() {
        // 注册这个 BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        // 开始查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //结束查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //查找设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备扫描模式改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //绑定状态
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(mReceiver, filter);

        if(!BlueToothController.getInstance().checkBlueToothIsOpen()){
            new AlertCommonDialog(bActivity, "您的蓝牙未打开，是否打开？", "打开", "取消"){
                @Override
                public void onPositiveClick() {
                    BlueToothController.getInstance().openBlueTooth(bActivity, REQUEST_CODE);
                }
            }.show();
        }
    }


    private void getBondedDeviceList() {
        clearRcy();
        List<BluetoothDevice> pairedDevices = BlueToothController.getInstance().getBondedDeviceList();
        if(pairedDevices != null){
            for(BluetoothDevice device:pairedDevices){
                // 把名字和地址取出来添加到适配器中
                adapter.addItem(new BluetoothDeviceInfo(device, 0));
            }
        }
    }


    private void findDevice() {
        clearRcy();
        new RxPermissions(bActivity)
                .requestEach(Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new PermissionConsumer(bActivity, true) {
                    @Override
                    public void success(Permission permission) {
                        BlueToothController.getInstance().findDevice();
                    }
                });


    }

    private void clearRcy(){
        adapter.refresh(new ArrayList<>());
        tv_content.setText("");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_CODE) {
            if( resultCode != RESULT_OK) {
                finish();
            }
        }
    }
}
