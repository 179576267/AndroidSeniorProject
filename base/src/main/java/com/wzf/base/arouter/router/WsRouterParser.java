package com.wzf.base.arouter.router;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.base.provider.BaseProviderManager;
import com.demo.base.rx.PermissionConsumer;
import com.demo.base.utils.ActivityCollector;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wzf.base.arouter.RouterPathConstant;

import java.io.Serializable;


public class WsRouterParser {

    public interface OpenDialogIm {
        void onDaySignDialog(Context context);

        void onUserInfoDialog(Context context, String tid);

        void onExchangeDialog(Context context, String itemId);
    }

    public static OpenDialogIm openDialogIm;

    public static void setOpenDialogIm(OpenDialogIm openDialogIm) {
        WsRouterParser.openDialogIm = openDialogIm;
    }

    public static Context getContext(Context context) {

        //初始化路由
        BaseProviderManager.getManager().initARouter();
        if (context == null) {
            context = ActivityCollector.getCurrentActivity();
        }
        return context == null ? BaseProviderManager.getManager().getApplication() : context;
    }



    //网页
    public static void intoWeb(Context context, String url, boolean hideShare) {
        context = getContext(context);
        ARouter.getInstance().build(RouterPathConstant.ACTIVITY_WEB)
                .withString("url", url)
                .withBoolean("hideShare", hideShare)
                .navigation(context);
    }

    public static void intoLogin(Context context) {
        intoLogin(context, null, null);
    }

    public static void intoLogin(Context context, NavigationCallback callback, String message) {
        context = getContext(context);
        ARouter.getInstance().build(RouterPathConstant.ACTIVITY_URL_LOGIN)
                .withString("message", message)
                .navigation(context, callback);
    }




}