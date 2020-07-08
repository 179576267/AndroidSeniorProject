package com.example.aspect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.aspect.aspect.CheckDoubleClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @CheckDoubleClick(100)
    public void onClick(View view) {
        Log.i("TestAspect", "onClick");
        String s = null;
        Log.i("TestAspect", String.valueOf(s));
        testAop();
    }


    public void testAop(){
        Log.i("TestAspect", "testAop");
    }

}
