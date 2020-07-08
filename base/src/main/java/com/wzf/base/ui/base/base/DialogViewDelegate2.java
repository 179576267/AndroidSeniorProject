package com.wzf.base.ui.base.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.demo.base.ui.base.delegate.ViewDelegate;
import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.demo.base.utils.SoftInputUtil;


public abstract class DialogViewDelegate2 extends ViewDelegate {
    protected boolean isCanOutsideCancel = true;
    protected View mVBg;
    protected View cl_container;

    public int getBgColor() {
        return 0xb2;
    }

    public boolean isCanOutsideCancel() {
        return isCanOutsideCancel;
    }

    public void setCanOutsideCancel(boolean canOutsideCancel) {
        isCanOutsideCancel = canOutsideCancel;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void show(Activity context) {
        show(context, null);
    }

    @Override
    public boolean onBackPressed() {
        dismiss();
        return true;
    }

    public void show(Activity context, Bundle bundle) {
        mContextOnViewParentDelegate = context;
        show(getActivityRoot(context), bundle);
    }

    public void show(ViewGroup contentParentView, Bundle bundle) {
        if (contentParentView == null) {
            return;
        }
        ViewDelegateHelper.getInstance().register(mContextOnViewParentDelegate, this);
        replaceContainer(contentParentView, bundle);
        animIn();
    }

    public int getContentViewLayoutParamsWidth() {
        return FrameLayout.LayoutParams.MATCH_PARENT;
    }

    public int getContentViewLayoutParamsHeight() {
        return FrameLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public View getLayoutView() {
        FrameLayout fl = new FrameLayout(mContextOnViewParentDelegate);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanOutsideCancel) {
                    dismiss();
                }
            }
        });
        fl.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mVBg = new View(mContextOnViewParentDelegate);
        int bgColor = getBgColor();
        mVBg.setBackgroundColor(bgColor << 24);
        fl.addView(mVBg, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        cl_container = getDialogView();
        if (cl_container == null) {
            Integer layoutId = getDialogLayoutId();
            if (layoutId != null) {
                cl_container = LayoutInflater.from(mContextOnViewParentDelegate).inflate(layoutId, null, false);
            }
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getContentViewLayoutParamsWidth(), getContentViewLayoutParamsHeight());
        params.topMargin = getRootViewPaddingTop();
//        cl_container.setLayoutParams(params);
        cl_container.setClickable(true);
        fl.addView(cl_container, params);
        return fl;
    }

    public int getRootViewPaddingTop() {
        return 0;
    }

    @Override
    public Integer getLayoutId() {
        return null;
    }

    public abstract Integer getDialogLayoutId();

    public View getDialogView() {
        return null;
    }


    protected void animIn() {
        if (mVBg == null) {
            return;
        }
        AlphaAnimation anim = new AlphaAnimation(0.1f, 1f);
        anim.setDuration(300);     //设置动画的过渡时间
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        cl_container.startAnimation(anim);
        mVBg.setBackgroundColor(0x0);
        mVBg.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                int bgColor = getBgColor();
                mVBg.setBackgroundColor(bgColor << 24);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mVBg.startAnimation(animation);
    }

    public void dismiss() {
        closeSoftInput();
        finishWithAnim();
    }

    public void closeSoftInput() {
        if (mContextOnViewParentDelegate != null)
            SoftInputUtil.closeSoftInput((Activity) mContextOnViewParentDelegate);
    }

    public void finishWithAnim() {
        if (mVBg == null) {
            finish();
            return;
        }
        animOut();
    }

    protected void animOut() {
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(300);
        AlphaAnimation anim = new AlphaAnimation(1f, 0f);
        anim.setDuration(300);     //设置动画的过渡时间

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (mVBg != null) {
            mVBg.setVisibility(View.GONE);
            mVBg.startAnimation(animation);
        }
        if (cl_container != null) {
            cl_container.setVisibility(View.GONE);
            cl_container.startAnimation(anim);
        }
    }

    public void finish() {
        closeSoftInput();
        if (mVBg != null) {
            mVBg.clearAnimation();
        }
        if (cl_container != null) {
            cl_container.clearAnimation();
        }
        boolean unregister = ViewDelegateHelper
                .getInstance().unregister(mContextOnViewParentDelegate, this);
        if (!unregister) {
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
