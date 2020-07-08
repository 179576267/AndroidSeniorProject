package com.wzf.changeskin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by wangzhenfei on 2019/10/17.
 */
public class SkinHelper {
    //单例
    private final static SkinHelper instance = new SkinHelper();

    public static SkinHelper getInstance() {
        return instance;
    }

    private Resources outResource;
    private Context mContext;
    private String outPkgName;


    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 加载外部资源包
     * @param path 外部的apk路径
     */
    public void load(final String path){
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        //获取PackageManager引用
        PackageManager mpm = mContext.getPackageManager();
        //解析某个路径下的apk信息
        PackageInfo packageInfo = mpm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        outPkgName = packageInfo.packageName;
        //存储包名
//        outPkgName = "com.wzf.skin_12";

        //获取AssetManager  调用 addAssetPath()方法
        AssetManager assetManager;

        try {
            assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.invoke(assetManager, path);

            outResource = new Resources(assetManager,//参数1，资源管理器
                    mContext.getResources().getDisplayMetrics(),//这个好像是屏幕参数
                    mContext.getResources().getConfiguration());//资源配置
            //最终创建出一个 "外部资源包"mOutResource ,它的存在，就是要让我们的app有能力加载外部的资源文件
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提供外部资源包里面的颜色
     * @param resId
     * @return
     */
    public int getColor(int resId){
        if(outResource == null){
            return resId;
        }

        String resName = mContext.getResources().getResourceEntryName(resId);
        int outResId = outResource.getIdentifier(resName, "color", outPkgName);
        if(outResId == 0){
            return resId;
        }

        return outResource.getColor(outResId);
    }

    /**
     * 提供外部资源包里的图片资源
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId) {//获取图片
        if (outResource == null) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        String resName = mContext.getResources().getResourceEntryName(resId);
        int outResId = outResource.getIdentifier(resName, "drawable", outPkgName);
        if (outResId == 0) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        return outResource.getDrawable(outResId);
    }

    /**
     * 提供外部资源包里的图片资源
     * @param resId
     * @return
     */
    public Drawable getMipmap(int resId) {//获取图片
        if (outResource == null) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        String resName = mContext.getResources().getResourceEntryName(resId);
        int outResId = outResource.getIdentifier(resName, "mipmap", outPkgName);
        if (outResId == 0) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        return outResource.getDrawable(outResId);
    }


    /**
     * 提供外部资源包里的文字大小
     * @param textSizeId
     * @return
     */
    public float getTextSize(int textSizeId) {
        if(outResource == null){
            return textSizeId;
        }

        String resName = mContext.getResources().getResourceEntryName(textSizeId);
        int outResId = outResource.getIdentifier(resName, "dimen", outPkgName);
        if(outResId == 0){
            return textSizeId;
        }

        return outResource.getDimension(outResId);

    }
}
