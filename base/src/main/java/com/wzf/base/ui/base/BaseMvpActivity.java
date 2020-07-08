package com.wzf.base.ui.base;

import android.os.Bundle;

import com.demo.base.ui.base.mvp.RxPresenter;


/**
 * Created by wangzhenfei on 2019/3/21.
 */
public abstract class BaseMvpActivity<P extends RxPresenter>  extends BaseActivity{
    public P presenter;

    @Override
    protected void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        presenter = setPresenter();
    }

    public abstract P setPresenter();

    @Override
    protected void onDestroy() {
        if(presenter != null){
            presenter.clear();
        }
        super.onDestroy();
    }
}
