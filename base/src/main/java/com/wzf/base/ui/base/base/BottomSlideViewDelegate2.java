package com.wzf.base.ui.base.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.demo.base.ui.base.delegate.ViewDelegate;
import com.demo.base.ui.base.delegate.ViewDelegateHelper;
import com.demo.base.utils.SoftInputUtil;
import com.wzf.base.R;

/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/7/19
 * @Description
 * @Project woshimi_dev
 * @Package com.demo.base.ui.base.delegate
 * @Copyright personal
 */

public abstract class BottomSlideViewDelegate2 extends ViewDelegate {

    protected View mBottomSlideContentView;
    protected View mVBg;
    protected View cl_container;
    protected BottomSheetBehavior behavior;

    public DelegateActivity getDelegateActivity() {
        if (mContextOnViewParentDelegate != null) {
            return (DelegateActivity) mContextOnViewParentDelegate;
        }

        return null;
    }
    public int getBgColor() {
        return 0xb2;
    }


    public BottomSlideViewDelegate2() {
    }

    public void show(DelegateActivity context) {
        show(context, null);
    }

    @Override
    public boolean onBackPressed() {
        dismiss();
        return true;
    }

    protected boolean isBehindFloatView() {
        return true;
    }

    protected void onAttachView(ViewGroup container, View child) {
        onDetachView(container, child);
        if (container != null && container != child) {
            //兼容悬浮框
            if (isBehindFloatView()) {
                int childCount = container.getChildCount();
                if (childCount > 0) {
                    View childAt = container.getChildAt(childCount - 1);
                    if (childAt != null) {
                        Object tag = childAt.getTag(R.id.id_key_float_state);
                        if (tag != null && tag instanceof Boolean) {
                            if ((Boolean) tag) {
                                container.addView(child, childCount - 1);
                                return;
                            }
                        }
                    }
                }
                container.addView(child);
            } else {
                container.addView(child);
            }

        }
    }
    public void show(DelegateActivity context, Bundle bundle) {
        mContextOnViewParentDelegate = context;
        show(getActivityRoot(context), bundle);
    }

    public void show(ViewGroup contentParentView, Bundle bundle) {
        if (contentParentView == null) {
            return;
        }
        ViewDelegateHelper.getInstance().register(mContextOnViewParentDelegate, this);
        replaceContainer(contentParentView, bundle);
        initBehavior();
        animIn();
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

    protected boolean isCanSlide() {
        return true;
    }

    boolean isCanOutsideCancel = true;

    public ConstraintLayout.LayoutParams getSlideContainerViewLayoutParams(){
        ConstraintLayout.LayoutParams params =
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                        , ConstraintLayout.LayoutParams.WRAP_CONTENT);

        return params;
    }

    public int getSlideContentViewLayoutParamsWidth(){
        return CoordinatorLayout.LayoutParams.MATCH_PARENT;
    }
    public int getSlideContentViewLayoutParamsHeight(){
        return CoordinatorLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public View getLayoutView() {
        ConstraintLayout fl = new ConstraintLayout(mContextOnViewParentDelegate);
//        fl.setClickable(true);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanOutsideCancel) {
                    fl.setOnClickListener(null);
                    fl.setClickable(true);
                    dismiss();
                }
            }
        });
        fl.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        mVBg = new View(mContextOnViewParentDelegate);
        mVBg.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        fl.addView(mVBg);

        if (isCanSlide()) {
            CoordinatorLayout cl = new CoordinatorLayout(mContextOnViewParentDelegate);
            ConstraintLayout.LayoutParams params = getSlideContainerViewLayoutParams();
            params.topMargin = getRootViewPaddingTop();
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            cl.setLayoutParams(params);
            fl.addView(cl);


            mBottomSlideContentView = getBottomSlideView();
            if (mBottomSlideContentView == null) {
                Integer layoutId = getBottomSlideLayoutId();
                if (layoutId != null) {
                    mBottomSlideContentView = LayoutInflater.from(mContextOnViewParentDelegate).inflate(layoutId, null, false);
                }else {
                    mBottomSlideContentView = getBottomSlideLayout();
                }
            }

            if (mBottomSlideContentView != null) {
                cl.addView(mBottomSlideContentView);
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mBottomSlideContentView.getLayoutParams();
                lp.width = getSlideContentViewLayoutParamsWidth();
                lp.height = getSlideContentViewLayoutParamsHeight();

                lp.setBehavior(new BottomSheetBehavior());
            }
            this.cl_container = cl;
        } else {
            cl_container = getBottomSlideView();
            if (cl_container == null) {
                Integer layoutId = getBottomSlideLayoutId();
                if (layoutId != null) {
                    cl_container = LayoutInflater.from(mContextOnViewParentDelegate).inflate(layoutId, null, false);
                }else {
                    mBottomSlideContentView = getBottomSlideLayout();
                }
            }

            ConstraintLayout.LayoutParams params = getSlideContainerViewLayoutParams();
            params.topMargin = getRootViewPaddingTop();
            cl_container.setLayoutParams(params);
            cl_container.setClickable(true);
            fl.addView(cl_container);
            mBottomSlideContentView = cl_container;
        }
        return fl;
    }

    public int getRootViewPaddingTop() {
        return 0;
    }

    @Override
    public Integer getLayoutId() {
        return null;
    }

    public abstract Integer getBottomSlideLayoutId();
    public View getBottomSlideLayout(){
        return null;
    }

    public View getBottomSlideView() {
        return null;
    }

    public boolean canHideable() {
        return true;
    }

    private void initBehavior() {

        if (!isCanSlide()) {
            return;
        }
        behavior = BottomSheetBehavior.from(mBottomSlideContentView);
        behavior.setHideable(canHideable());
//        behavior.setPeekHeight(400);
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                STATE_DRAGGING: 被拖拽状态
//                STATE_SETTLING: 拖拽松开之后到达终点位置（collapsed or expanded）前的状态。
//                STATE_EXPANDED: 完全展开的状态。
//                STATE_COLLAPSED: 折叠关闭状态。可通过app:behavior_peekHeight来设置显示的高度,peekHeight默认是0。
//                STATE_HIDDEN: 隐藏状态。默认是false，可通过app:behavior_hideable属性设置。


                //这里是bottomSheet状态的改变
                if (newState == BottomSheetBehavior.STATE_HIDDEN
                        || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //todo
                    finish();
                }
//                DebugLog.d("behavior", "newState:" + newState);
            }

            /**
             * @param bottomSheet
             * @param slideOffset
             */
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画

                if (mVBg != null) {
                    int bgColor = getBgColor();
                    int c = (int) (bgColor * slideOffset);
                    if (c > bgColor) {
                        c = bgColor;
                    }
                    mVBg.setBackgroundColor(c << 24);
                }
//                DebugLog.d("behavior", "slideOffset:" + slideOffset);
            }
        });
    }


    private void animIn() {
        if (mVBg == null) {
            return;
        }
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为300ms
        final TranslateAnimation anim = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        anim.setDuration(300);     //设置动画的过渡时间
        anim.setInterpolator(new AccelerateDecelerateInterpolator());

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (behavior != null)
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
        ViewDelegateHelper.getInstance().removeViewDelegateIml(this);
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
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(300);
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为300ms
        TranslateAnimation anim = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
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

        if (mVBg != null)
            mVBg.startAnimation(animation);
        if (cl_container != null)
            cl_container.startAnimation(anim);
    }

    public void finish() {
        closeSoftInput();
        if (mVBg != null) {
            mVBg.clearAnimation();
        }
        if (cl_container != null) {
            cl_container.clearAnimation();
        }
        ViewDelegateHelper
                .getInstance().unregister(this);
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
