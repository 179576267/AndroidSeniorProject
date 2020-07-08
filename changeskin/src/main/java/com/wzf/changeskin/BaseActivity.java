package com.wzf.changeskin;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.io.File;

/**
 * Created by wangzhenfei on 2019/10/17.
 */
public class BaseActivity extends AppCompatActivity {
    protected static String[] skins = new String[]{"skin1.apk", "skin1.apk"};

    private static String mCurrentSkin = null;

    private SkinFactory factory;

    private boolean isAllowChangeSkin = true;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};//内存读写的权限，现在要动态申请了？

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(isAllowChangeSkin){
            factory = new SkinFactory();
            factory.setDelegate(getDelegate());
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            layoutInflater.setFactory2(factory);
        }
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);//申请权限。。
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentSkin != null) {
            changeSkin(mCurrentSkin);
        }
    }

    /**
     * 正正换肤
     * @param path
     */
    public void changeSkin(String path) {
        if(isAllowChangeSkin){
            File skFile = new File("/sdcard/aaa_skin/", path);
            if(skFile.exists()){
                SkinHelper.getInstance().load(skFile.getAbsolutePath());
//                factory.changeSkin();
                mCurrentSkin = path;
            }else {
                Toast.makeText(this, "路径不存在", Toast.LENGTH_LONG).show();
            }

        }
    }
}
