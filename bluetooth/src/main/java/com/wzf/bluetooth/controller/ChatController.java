package com.wzf.bluetooth.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.wzf.bluetooth.connect.AcceptThread;
import com.wzf.bluetooth.connect.ConnectThread;
import com.wzf.bluetooth.protocaol.ChatProtocol;

public class ChatController {
    private static class InnerClass {
        private static ChatController singletonInstance = new ChatController();
    }
    
    private ChatController(){}
    
    public static ChatController getInstance(){
            return InnerClass.singletonInstance;
    }


    private ConnectThread mConnectThread;
    private AcceptThread mAcceptThread;
    private ChatProtocol mProtocol = new ChatProtocol();

    /**
     * 与服务器连接进行聊天
     * @param device
     * @param adapter
     * @param handler
     */
    public void startChatWith(BluetoothDevice device, BluetoothAdapter adapter, Handler handler){
        mConnectThread = new ConnectThread(device,adapter,handler);
        mConnectThread.start();
    }


    /**
     * 等待客户端来连接
     * @param adapter
     * @param handler
     */
    public void waitingForFriends(BluetoothAdapter adapter, Handler handler) {
        mAcceptThread = new AcceptThread(adapter,handler);
        mAcceptThread.start();
    }


    /**
     * 发出消息
     * @param msg
     */
    public void sendMessage(String msg) {
        byte[] data = mProtocol.encodePackage(msg);
        if(mConnectThread != null) {
            mConnectThread.sendData(data);
        }
        else if( mAcceptThread != null) {
            mAcceptThread.sendData(data);
        }
    }


    /**
     * 网络数据解码
     * @param data
     * @return
     */
    public String decodeMessage(byte[] data) {
        return  mProtocol.decodePackage(data);
    }

    /**
     * 停止聊天
     */
    public void stopChat() {
        if(mConnectThread != null) {
            mConnectThread.cancel();
        }
        else if( mAcceptThread != null) {
            mAcceptThread.cancel();
        }
    }

}
