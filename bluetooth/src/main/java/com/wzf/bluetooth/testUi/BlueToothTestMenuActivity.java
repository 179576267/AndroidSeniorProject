package com.wzf.bluetooth.testUi;

import android.os.Bundle;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.wzf.base.ui.activity.BaseMenuActivity;

public class BlueToothTestMenuActivity extends BaseMenuActivity {

    @Override
    public void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        addChild("扫描测试", BlueToothScanActivity.class);
        addChild("点击测试", v -> ToastUtils.show("点击测试"));
    }
}
