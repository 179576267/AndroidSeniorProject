package com.wzf.base.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.demo.base.ui.base.BaseDialog;
import com.demo.base.ui.view.shape.ShapeTextView;
import com.wzf.base.R;

import butterknife.ButterKnife;

/**
 * Created by wangzhenfei on 2016/10/12.
 * 通用的提示窗口
 */
public class AlertCommonDialog extends BaseDialog implements View.OnClickListener {
    TextView tvContent;
    ShapeTextView tvPositive;
    ShapeTextView tvNegative;

    private String content;
    private String positiveText;
    private String negativeText;

    private boolean cancelOutside = true;

    /**
     * @param context
     * @param content      内容
     * @param positiveText 确定
     * @param negativeText 取消 只需要一个按钮传null
     */
    public AlertCommonDialog(Context context, String content, String positiveText, String negativeText) {
        this(context, content, positiveText, negativeText, true);
    }

    /**
     * @param context
     * @param content      内容
     * @param positiveText 确定
     * @param negativeText 取消 只需要一个按钮传null
     * @param isCancel     点击弹框其他地方是否消失 默认false
     */
    public AlertCommonDialog(Context context, String content, String positiveText, String negativeText, boolean isCancel) {
        super(context);
        mContext = (Activity) context;
        this.content = content;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.cancelOutside = isCancel;
    }



    @Override
    protected void init(Bundle savedInstanceState) {
        tvContent = findViewById(R.id.tv_content);
        tvPositive = findViewById(R.id.tv_positive);
        tvNegative = findViewById(R.id.tv_negative);
        tvPositive.setOnClickListener(this::onClick);
        tvNegative.setOnClickListener(this::onClick);
        ButterKnife.bind(this);
        initDatas();
        setPositiveBtn(tvPositive);
        setNegativeBtn(tvNegative);

        setCanceledOnTouchOutside(cancelOutside);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_common_alert;
    }

    @Override
    protected float getWithRate() {
        return 0.776f;
    }

    @Override
    protected int getAnimationId() {
        return R.style.AnimScale;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    public void setNegativeBtn(ShapeTextView tvNegative) {
        
    }

    public void setPositiveBtn(ShapeTextView tvPositive) {

    }

    private void initDatas() {
        if (TextUtils.isEmpty(negativeText)) {
            tvNegative.setVisibility(View.GONE);
        } else {
            tvNegative.setText(negativeText);
        }
        if(!TextUtils.isEmpty(content) && content.length() > 50){
            tvContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvContent.setText( "        " + content);
        }else {
            tvContent.setText(content);
        }
        tvPositive.setText(positiveText);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void onClick(View view) {
        try {
        if(view.getId() == R.id.tv_positive){
            onPositiveClick();
        }else if(view.getId() == R.id.tv_negative){
            onNegativeClick();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismiss();


    }

    public void onPositiveClick() {

    }

    public void onNegativeClick() {

    }

}
