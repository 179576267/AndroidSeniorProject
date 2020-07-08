package com.wzf.hotfix;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangzhenfei on 2019/10/22.
 */
public class TestClass {
    public  void test(Context context){
        String s = null;
        Toast.makeText(context, s.toString(), Toast.LENGTH_LONG).show();
    }
}
