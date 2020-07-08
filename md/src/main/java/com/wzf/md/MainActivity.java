package com.wzf.md;

import android.os.Bundle;

import com.wzf.base.ui.activity.BaseMenuActivity;
import com.wzf.md.surface.SurfaceActivity;

public class MainActivity extends BaseMenuActivity {

    @Override
    public void initForSave(Bundle bundle) {
        addChild("behavior", BehaviorActivity.class);
        addChild("surface", SurfaceActivity.class);
    }
}
