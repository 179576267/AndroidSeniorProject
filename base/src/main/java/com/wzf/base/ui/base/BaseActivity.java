package com.wzf.base.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.demo.base.e.E_SystemBar;
import com.demo.base.ui.base.BaseLibActivity;
import com.demo.base.utils.DebugLog;
import com.demo.base.utils.PreferencesHelper;
import com.demo.base.utils.SystemTimeUtil;

/**
 * @author zhenfei.wang
 * @date 2016/8/8
 */
public class BaseActivity extends BaseLibActivity {
    PreferencesHelper helper;

    long timeMillis;
    long timeNMillis;


    public void logTime(String tag) {
        timeNMillis = SystemTimeUtil.getCurrentTimeMillis();
        DebugLog.d(getClass().getSimpleName(), tag + ":" + (timeNMillis - timeMillis));

        timeMillis = timeNMillis;
    }

    @Override
    public int getLayoutId() {
        fullScreen(E_SystemBar.BLACK_SYSTEM_BAR);
        return super.getLayoutId();
    }

    @Override
    protected void onDestroy() {
//        MessageReceiverManager.getInstance().removeWhite(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void bundleToIntentString(String key, Bundle bundle) {
        getIntent().putExtra(key, bundle.getString(key));
    }

    protected void bundleToIntentInt(String key, Bundle bundle, int defaultValue) {
        getIntent().putExtra(key, bundle.getInt(key, defaultValue));
    }

    protected void bundleToIntentLong(String key, Bundle bundle, long defaultValue) {
        getIntent().putExtra(key, bundle.getLong(key, defaultValue));
    }

    protected void bundleToIntentByteArray(String key, Bundle bundle) {
        getIntent().putExtra(key, bundle.getByteArray(key));
    }

    protected void bundleToIntentBool(String key, Bundle bundle, boolean defaultValue) {
        getIntent().putExtra(key, bundle.getBoolean(key, defaultValue));
    }

    protected void intentToBundleString(String key, Bundle bundle) {
        String value = getIntent().getStringExtra(key);
        bundle.putString(key, value);
    }

    protected void intentToBundleInt(String key, Bundle bundle, int defaultValue) {
        int value = getIntent().getIntExtra(key, defaultValue);
        bundle.putInt(key, value);
    }

    protected void intentToBundleBool(String key, Bundle bundle, boolean defaultValue) {
        boolean value = getIntent().getBooleanExtra(key, defaultValue);
        bundle.putBoolean(key, value);
    }


    public PreferencesHelper getHelper() {
        if (helper == null) {
            helper = new PreferencesHelper(bActivity, PreferencesHelper.TB_MESSAGE);
        }
        return helper;

    }

    //取出并消费
    public String[] getRouterData() {
        Intent intent = getIntent();

        String[] data_router = intent.getStringArrayExtra("data_router");
        if (data_router != null) {
            intent.removeExtra("data_router");
        }

        return data_router;
    }

    //当前页面路由
    protected String mDataHeader;
    //下级路由
    protected String[] mDataNextRouter;

    public String getRouter() {
        return mDataHeader;
    }

    public String[] getNextRouters() {
        return mDataNextRouter;
    }

    public boolean hasNextRouters() {
        return mDataNextRouter != null && mDataNextRouter.length > 0;
    }

    public void dispatchRouter(String[] dataRouter) {
        if (dataRouter != null && dataRouter.length > 0) {

            //取出当前路由
            mDataHeader = dataRouter[0];

            int length = dataRouter.length;

            //当前路由不为空，且还有下级路由
            if (mDataHeader != null && length > 1) {

                mDataNextRouter = new String[length - 1];

                for (int i = 1; i < length; i++) {
                    mDataNextRouter[i - 1] = dataRouter[i];
                }
            }
            dispatchRouter();
        }
    }

    public void dispatchRouter() {

    }


    //消费当前路由
    public String consumeRouter() {
        String router = mDataHeader;
        //当前页面路由
        mDataHeader = null;
        return router;
    }

    //消费当前路由
    public Pair<String, String[]> consumeAllRouter() {

        Pair<String, String[]> pair = new Pair<>(mDataHeader, mDataNextRouter);
        //当前页面路由
        mDataHeader = null;
        //下级路由
        mDataNextRouter = null;

        return pair;
    }

    public String[] consumeNextRouter() {

        Pair<String, String[]> pair = new Pair<>(mDataHeader, mDataNextRouter);
        String[] next = mDataNextRouter;
        //下级路由
        mDataNextRouter = null;

        return next;
    }

}
