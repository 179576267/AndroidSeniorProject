package com.wzf.base.ui.base.base;

import android.os.Bundle;
import android.view.View;

import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.demo.base.ui.base.delegate.ViewParentDelegate;


/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/1/3
 * @Description
 * @Project im_dev
 * @Package com.wzf.fan.ui.delegate.base
 * @Copyright personal
 */

public abstract class ViewDelegateActivity extends DelegateActivity {

    protected ViewParentDelegate mViewDelegate;

    @Override
    public int getLayoutId() {
        return -1;
    }

    @Override
    public View getLayoutView() {
        Class<? extends ViewParentDelegate> viewDelegate = getViewDelegate();
        if (viewDelegate == null) {
            return null;
        }
        mViewDelegate = ViewDelegateHelper
                .build(this, viewDelegate);
        return mViewDelegate.inflateContentView(getLayoutInflater(),  getViewDelegateBundle());
    }

    @Override
    protected void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        if (mViewDelegate != null)
            mViewDelegate.onCreateView();
    }

    public Bundle getViewDelegateBundle() {
        return null;
    }

    public abstract Class<? extends ViewParentDelegate> getViewDelegate();



}
