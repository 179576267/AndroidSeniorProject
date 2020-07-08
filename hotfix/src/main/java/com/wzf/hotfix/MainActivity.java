package com.wzf.hotfix;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};//内存读写的权限，现在要动态申请了？
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);//申请权限。。
        setContentView(R.layout.activity_main);
    }

    /**
     * 申请权限，为了要把外部文件写入到 手机内存中
     *
     * @param activity
     */
    public static void verifyStoragePermissions(AppCompatActivity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,PERMISSIONS_STORAGE[1]);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_fix){
            FixBugUtils.copyDexToAppAndFix(this.getApplicationContext(),"classFix.dex");
        }else  if(v.getId() == R.id.tv_test){
            new TestClass().test(this);
        }
    }
}
