package com.wzf.base.ui.base.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.hjq.toast.ToastUtils;
import com.wzf.base.ui.base.LazyLoadFragment;

/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/1/3
 * @Description
 * @Project im_dev
 * @Package com.wzf.fan.ui.delegate
 * @Copyright personal
 */

public abstract class DelegateFragment extends LazyLoadFragment {

    protected boolean isFinishActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFinishActivity = false;
    }

    @Override
    public void onDestroyView() {
        ViewDelegateHelper.getInstance().unregister(this);
        super.onDestroyView();
    }

    public void runOnUiThread(Runnable action){
        if (getActivity() != null){
            getActivity().runOnUiThread(action);
        }
    }

    @Override
    protected void loadData() {

    }

    public void toast(String content){
        ToastUtils.show(content);
    }


    public void finishActivity() {
        if (getActivity() != null) {
            isFinishActivity = true;
            getActivity().finish();
        }
    }

    public boolean isFinishingActivity() {
        return isFinishActivity;
    }

    public boolean isRelease() {
        return false;
    }

    public abstract class RunTask implements Runnable {

        public abstract void run2();

        @Override
        public void run() {
            if (!isRelease()) {
                run2();
            }
        }
    }

}
