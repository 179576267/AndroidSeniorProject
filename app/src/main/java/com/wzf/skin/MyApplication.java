package com.wzf.skin;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;

import com.demo.base.i.OnShareSuccessListener;
import com.demo.base.provider.BaseModuleProvider;
import com.demo.base.provider.BaseProviderManager;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastQQStyle;
import com.wzf.changeskin.SkinHelper;

/**
 * Created by wangzhenfei on 2019/10/17.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinHelper.getInstance().init(this);
        ToastUtils.init(this, new ToastQQStyle());
        BaseProviderManager.getManager().initProviderInApp(new BaseModuleProvider() {
            @Override
            public Application getApplication() {
                return MyApplication.this;
            }

            @Override
            public String getUid() {
                return null;
            }

            @Override
            public String getToken() {
                return null;
            }

            @Override
            public boolean isDebug() {
                return false;
            }

            @Override
            public Dialog getShareDialog(Context context, String title, String content, String imageUrl, String shareLink, int platformType, OnShareSuccessListener listener) {
                return null;
            }

            @Override
            public String getFileProviderAuthority() {
                return null;
            }

            @Override
            public void onGameCloseFloat(boolean isGaming) {

            }

            @Override
            public boolean checkFloatWindow(Context context, int roomNum, int nextType,
                                            Runnable runnable) {
                return false;
            }

            @Override
            public boolean hasVoiceChat() {
                return false;
            }

            @Override
            public void initARouter() {

            }

            @Override
            public void initAsyncFirstActivityOnCreate(Runnable runnable) {

            }

            @Override
            public void initAfterFirstActivityDisplayed() {

            }

            @Override
            public void intoLoginPage() {

            }

            @Override
            public void checkMqttConnect() {

            }
        });
    }
}
