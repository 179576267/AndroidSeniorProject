package com.wzf.base.ui.base.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.demo.base.ui.base.delegate.ViewParentDelegate;

/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/1/3
 * @Description
 * @Project im_dev
 * @Package com.wzf.fan.ui.delegate
 * @Copyright personal
 */

public abstract class ViewDelegateFragment<T extends ViewParentDelegate> extends DelegateFragment {

    protected T mViewDelegate;

    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public View getRootView(LayoutInflater inflater) {
        mViewDelegate = ViewDelegateHelper
                .build(this, buildViewDelegate());
        return mViewDelegate.inflateContentView(inflater,  getViewDelegateBundle());
    }

    @Override
    public void init() {
        mViewDelegate.onCreateView();
    }

    public Bundle getViewDelegateBundle() {
        return getArguments();
    }

    public abstract Class<? extends ViewParentDelegate> buildViewDelegate();
}
