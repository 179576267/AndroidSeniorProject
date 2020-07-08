package com.wzf.base.ui.base.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.demo.base.ui.base.delegate.ViewDelegateIml;
import com.wzf.base.ui.base.BaseActivity;

import java.util.List;


/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/1/3
 * @Description
 * @Project im_dev
 * @Package com.wzf.fan.ui.delegate.base
 * @Copyright personal
 */

public abstract class DelegateActivity extends BaseActivity {

    @Override
    public void dispatchRouter() {
        super.dispatchRouter();
        dispatchRouter2Delegate();
    }

    public void dispatchRouter2Delegate() {
        ViewDelegateHelper.getInstance().dispatchRouter(this);
    }

    @Override
    protected void onDestroy() {
        ViewDelegateHelper.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ViewDelegateHelper.getInstance().onRestart(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewDelegateHelper.getInstance().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewDelegateHelper.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewDelegateHelper.getInstance().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ViewDelegateHelper.getInstance().onStop(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ViewDelegateHelper.getInstance().onNewIntent(this,intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ViewDelegateHelper.getInstance().onSaveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ViewDelegateHelper.getInstance().onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerNetStatusChangeListener() {

//        new RxPermissions(this)
//                .requestEach(Manifest.permission.CHANGE_NETWORK_STATE)
//                .subscribe(new PermissionConsumer(bActivity) {
//                    @Override
//                    public void success(Permission permission) {
//                        try {
//                            registerNetListener();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

    }

    private void registerNetListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    /**
                     * 网络可用的回调
                     * */
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        onNetAvailable();
                    }

                    /**
                     * 网络丢失的回调
                     * */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);

                        onNetLost();
                    }

                    /**
                     * 当建立网络连接时，回调连接的属性
                     * */
                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);

                    }

                    /**
                     *  按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                     *
                     *  之后在仔细的研究
                     * */
                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);

                    }

                    /**
                     * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                     * */
                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                    }

                    /**
                     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                     * */
                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                    }

                });
            }
        }
    }

    public void onNetAvailable() {

    }

    public void onNetLost() {

    }

    @Override
    public void onBackPressed() {
        if (onDelegateBackPressed()) {
            return;
        }
        super.onBackPressed();

    }

    public boolean onDelegateBackPressed() {
        List<ViewDelegateIml> delegates = ViewDelegateHelper.getInstance().getDelegates(this);
        if (delegates != null && delegates.size() > 0) {
            int size = delegates.size();
            for (int i = size - 1; i > -1; i--) {
                ViewDelegateIml d = delegates.get(i);
                if (d != null) {
                    if (d.onBackPressed()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
