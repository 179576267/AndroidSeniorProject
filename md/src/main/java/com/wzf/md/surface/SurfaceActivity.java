package com.wzf.md.surface;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.wzf.md.R;

public class SurfaceActivity extends BaseActivity {
    MSurfaceView surfaceView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_surface;
    }


    @Override
    public void initForSave(Bundle savedInstanceState) {
       surfaceView =  findViewById(R.id.surface);
//        surfaceView.loadHolder();
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.size_2:
                surfaceView.changeSize(2);
                break;
            case R.id.size_10:
                surfaceView.changeSize(10);
                break;
            case R.id.size_20:
                surfaceView.changeSize(20);
                break;
            case R.id.color_red:
                surfaceView.changeColor(Color.RED);
                break;
            case R.id.color_blue:
                surfaceView.changeColor(Color.BLUE);
                break;
            case R.id.color_green:
                surfaceView.changeColor(Color.GREEN);
                break;
            case R.id.clear:
                surfaceView.clear();
                break;
        }
    }
}


