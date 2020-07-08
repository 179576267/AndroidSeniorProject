package com.wzf.base.arouter;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.base.utils.DebugLog;

/**
 */
public class ARouterUtils {

    /**
     * 初始化router
     *
     * @param application
     */
    public static void initARouter(Application application) {
        ARouter.init(application);
    }

    /**
     * 在activity中添加
     *
     * @param activity activity
     */
    public static void injectActivity(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        ARouter.getInstance().inject(activity);
    }

    /**
     * 在fragment中添加
     *
     * @param fragment fragment
     */
    public static void injectFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        ARouter.getInstance().inject(fragment);
    }

    /**
     * 销毁资源
     */
    public static void destroy() {
        DebugLog.i("销毁路由资源");
        ARouter.getInstance().destroy();
    }

    /**
     * 简单的跳转页面
     *
     * @param string string目标界面对应的路径
     */
    public static void navigation(String string) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string)
                .navigation();
    }

    /**
     * 简单的跳转页面
     *
     * @param string string目标界面对应的路径
     */
    public static void navigation(String string, String flag) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string)
                .navigation();
    }

    /**
     * 简单的跳转页面
     *
     * @param string string目标界面对应的路径
     */
    public static void navigationGroup(String string, String group) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string, group)
                .navigation();
    }

    /**
     * 简单的跳转页面
     *
     * @param string string目标界面对应的路径
     */
    public static void navigation(String string, Context context) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string)
                .withTransition(com.demo.base.R.anim.slide_in_from_right, com.demo.base.R.anim.slide_out_to_left)
                .navigation(context);
    }

    /**
     * 简单的跳转页面
     *
     * @param string string目标界面对应的路径
     */
    public static void navigation(String string, Activity context, int requestCode) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string)
                .withTransition(com.demo.base.R.anim.slide_in_from_right, com.demo.base.R.anim.slide_out_to_left)
                .navigation(context, requestCode);
    }

    /**
     * 简单的跳转页面
     *
     * @param string   string目标界面对应的路径
     * @param callback 监听路由过程
     */
    public static void navigation(String string, Context context, NavigationCallback callback) {
        if (string == null) {
            return;
        }
        ARouter.getInstance()
                .build(string)
                .withTransition(com.demo.base.R.anim.slide_in_from_right, com.demo.base.R.anim.slide_out_to_left)
                .navigation(context, callback);
    }


    /**
     * 简单的跳转页面
     *
     * @param uri uri
     */
    public static void navigation(Uri uri) {
        if (uri == null) {
            return;
        }
        ARouter.getInstance()
                .build(uri)
                .navigation();
    }


    /**
     * 简单的跳转页面
     *
     * @param string    string目标界面对应的路径
     * @param bundle    bundle参数
     * @param enterAnim 进入时候动画
     * @param exitAnim  退出动画
     */
    public static void navigation(String string, Context context, Bundle bundle, int enterAnim, int exitAnim) {
        if (string == null) {
            return;
        }
        if (bundle == null) {
            ARouter.getInstance()
                    .build(string)
                    .withTransition(enterAnim, exitAnim)
                    .navigation(context);
        } else {
            ARouter.getInstance()
                    .build(string)
                    .with(bundle)
                    .withTransition(enterAnim, exitAnim)
                    .navigation(context);
        }
    }


    /**
     * 携带参数跳转页面
     *
     * @param path   path目标界面对应的路径
     * @param bundle bundle参数
     */
    public static void navigation(String path, Bundle bundle) {
        if (path == null || bundle == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .with(bundle)
                .navigation();
    }


    /**
     * 跨模块实现ForResult返回数据（activity中使用）,在fragment中使用不起作用
     * 携带参数跳转页面
     *
     * @param path   path目标界面对应的路径
     * @param bundle bundle参数
     */
    public static void navigation(String path, Bundle bundle, Activity context, int code) {
        if (path == null) {
            return;
        }
        if (bundle == null) {
            ARouter.getInstance()
                    .build(path)
                    .navigation(context, code);
        } else {
            ARouter.getInstance()
                    .build(path)
                    .with(bundle)
                    .navigation(context, code);
        }
    }

    /**
     * 共享元素与resultCode一起使用
     *
     * @param path
     * @param context
     * @param code
     * @param optionsCompat
     */
    public static void navigation(String path, Activity context, int code, ActivityOptionsCompat optionsCompat) {
        if (path == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .withOptionsCompat(optionsCompat)
                .navigation(context, code);
    }


    /**
     * 使用绿色通道(跳过所有的拦截器)
     *
     * @param path  path目标界面对应的路径
     * @param green 是否使用绿色通道
     */
    public static void navigation(String path, boolean green) {
        if (path == null) {
            return;
        }
        if (green) {
            ARouter.getInstance()
                    .build(path)
                    .greenChannel()
                    .navigation();
        } else {
            ARouter.getInstance()
                    .build(path)
                    .navigation();
        }
    }

    public static NavigationCallback getCallback() {
        NavCallback navCallback = new NavCallback() {
            @Override
            public void onArrival(Postcard postcard) {
                DebugLog.i("ARouterUtils" + "---跳转完了");
            }

            @Override
            public void onFound(Postcard postcard) {
                super.onFound(postcard);
                DebugLog.i("ARouterUtils" + "---找到了");
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                super.onInterrupt(postcard);
                DebugLog.i("ARouterUtils" + "---被拦截了");
            }

            @Override
            public void onLost(Postcard postcard) {
                super.onLost(postcard);
                DebugLog.i("ARouterUtils" + "---找不到了");
                //降级处理
                //DegradeServiceImpl degradeService = new DegradeServiceImpl();
                //degradeService.onLost(ActivityUtils.getApp(),postcard);

                //无法找到路径，作替换处理
//                PathReplaceServiceImpl pathReplaceService = new PathReplaceServiceImpl();
//                pathReplaceService.replacePath(ARouterConstant.ACTIVITY_ANDROID_ACTIVITY,ARouterConstant.ACTIVITY_DOU_MUSIC_ACTIVITY);
            }
        };
        return navCallback;
    }


}
