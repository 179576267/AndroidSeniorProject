package com.wzf.changeskin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by wangzhenfei on 2019/10/21.
 */
public class PlusResource extends Resources {

    public PlusResource(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }


    public static AssetManager getAssetManager(File file, Resources resources){
        AssetManager manager = resources.getAssets();
        if(file == null || !file.exists()){
           return manager;
        }
        try {
            Class<AssetManager> assetManagerClass = AssetManager.class;
            manager = AssetManager.class.newInstance();
            Method addAssetPath = assetManagerClass.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(manager, file.getAbsolutePath());
        } catch (Exception e) {
            manager = resources.getAssets();
        }
        return manager;
    }

    public static PlusResource getInstance(AssetManager assetManager, Resources resources){
        return new PlusResource(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }


    public static void main(String[] args) {
        File file = new File("");
        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(), file.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
    }
}
