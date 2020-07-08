package com.wzf.base.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.demo.base.rx.RunDelayObserver;
import com.demo.base.ui.base.BaseLibFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author zhenfei.wang
 * @date 2016/9/9
 */
public abstract class BaseFragment extends BaseLibFragment {
    protected BaseActivity bActivity;

    protected void onAttachToContext(Context context) {
        bActivity = (BaseActivity) context;
    }

    @Override
    public void attachBActivity() {
        if (bActivity == null) {
            bActivity = (BaseActivity) getActivity();
        }
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


    public RunDelayObserver runDelay(long delayMills, Runnable action) {

        if (delayMills < 1) {
            action.run();
            return null;
        }
        RunDelayObserver observer ;
        Observable.timer(delayMills, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer =new RunDelayObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        super.onSubscribe(disposable);
                        addReqDisposable(disposable);
                    }

                    @Override
                    public void run() {
                        if (action != null) {
                            action.run();
                        }
                    }

                });

        return observer;
    }
}
