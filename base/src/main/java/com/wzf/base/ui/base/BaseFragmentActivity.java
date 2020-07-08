package com.wzf.base.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.demo.base.R2;
import com.demo.base.ui.view.TitleTextView;
import com.wzf.base.R;
import com.wzf.base.arouter.ARouterUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 只加载一个fragment
 */
public abstract class BaseFragmentActivity extends BaseActivity {
    @BindView(R2.id.tv_center)
    protected TitleTextView tvCenter;
    @BindView(R2.id.im_right1)
    protected ImageView imRight1;
    @BindView(R2.id.top_header)
    RelativeLayout topHeader;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_base_fragment;
    }

    protected abstract void setFullScreen();

    @Override
    protected void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        ARouterUtils.injectActivity(this);
        topHeader.setVisibility(showTitleBar());
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, getFragment()).commitAllowingStateLoss();
    }

    protected abstract int showTitleBar();

    public abstract Fragment getFragment();


    @OnClick({R2.id.im_left, R2.id.im_right1})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.im_left) {
            finish();
        } else if (id == R.id.im_right1) {

        }
    }
}
