package com.wzf.skin;

import android.os.Bundle;

import com.wzf.base.ui.activity.BaseMenuActivity;
import com.wzf.bluetooth.testUi.BlueToothTestMenuActivity;

public class MenuActivity extends BaseMenuActivity {
    @Override
    public void initForSave(Bundle savedInstanceState) {
        super.initForSave(savedInstanceState);
        addChild("蓝牙模块", BlueToothTestMenuActivity.class);
    }
}
