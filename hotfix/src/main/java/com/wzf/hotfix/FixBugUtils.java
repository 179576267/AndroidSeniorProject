package com.wzf.hotfix;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by wangzhenfei on 2019/10/22.
 */
public class FixBugUtils {
    public static final String DEX_END_NAME = ".dex";
    private static final String FIX_DEX_PATH = "fix_dex";
    private static final String DEX_OPT_DIR = "opt_dir";

    public static final String DEX_BASECLASSLOADER_CLASS_NAME = "dalvik.system.BaseDexClassLoader";
    public static final String DEX_ELEMENTS_FIELD = "dexElements";//pathList中的dexElements字段
    public static final String DEX_PATHLIST_FIELD = "pathList";//BaseClassLoader中的pathList字段

    public static void copyDexToAppAndFix(Context context, String dexFileName) {
        File path = new File("/sdcard/androidSenior/hotfix//", dexFileName);
        //检测补丁是否存在
        if(!path.exists() || !dexFileName.endsWith(DEX_END_NAME)){
            Toast.makeText(context, "补丁包不存在", Toast.LENGTH_LONG).show();
            return;
        }

        //将文件copy到 /data/data/packageName/base/ 下
        File dexFilePath = context.getDir(FIX_DEX_PATH, Context.MODE_PRIVATE);
        File dexFile = new File(dexFilePath, dexFileName);
        if(dexFile.exists()){
            dexFile.delete();
        }

        InputStream is;
        OutputStream os;
        try {
            is = new FileInputStream(path);
            os = new FileOutputStream(dexFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            
            if(dexFile.exists()){
                loadMergeDex(context, dexFilePath, dexFile);
            }
            
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private static void loadMergeDex(Context context, File fexDexPath, File dexFile) {
        
        //加载本身的dex，通过PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();

        //加载硬盘中的dex文件
        //创建optimize路径
        File optimizeDir = new File(fexDexPath, DEX_OPT_DIR);
        if(!optimizeDir.exists()){
            optimizeDir.mkdir();
        }
        //dex文件路径， 优化输出路径， null， 父加载器
        DexClassLoader dexClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(),
                optimizeDir.getAbsolutePath(),
                null,
                pathClassLoader
                );

        try {
            //获取app自身的BaseDexClassLoader中的pathList字段
            Object appDexPathList = getDexPathListFile(pathClassLoader);

            //获取补丁的BaseDexClassLoader中的pathList字段
            Object fixDexPathList = getDexPathListFile(dexClassLoader);

            Object appDexElements = getDexElements(appDexPathList);
            Object fixDexElements = getDexElements(fixDexPathList);

            //合并两个elements的数据，将修复的dex插入到数组最前面
            Object finalElements = combineArray(fixDexElements, appDexElements);
            //给app 中的dex pathList 中的dexElements 重新赋值
            setField(appDexPathList, appDexPathList.getClass(), DEX_ELEMENTS_FIELD, finalElements);
            Toast.makeText(context, "修复成功!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 获得pathList中的dexElements
     *
     * @param obj
     * @return
     */
    public static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(obj,  obj.getClass(), DEX_ELEMENTS_FIELD);
    }

    /**
     * 获取ClassLoader 中pathList字段
     * @param pathClassLoader
     */
    private static Object getDexPathListFile(ClassLoader pathClassLoader) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(pathClassLoader,Class.forName(DEX_BASECLASSLOADER_CLASS_NAME) , DEX_PATHLIST_FIELD);
    }


    /**
     * 反射获取字段
     * @param o
     * @param fileName
     */
    public static Object getField(Object o, Class cls,String fileName) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(fileName);
        field.setAccessible(true);
        return field.get(o);
    }

    /**
     * 反射设置字段
     * @param o
     * @param fileName
     */
    public static  void setField(Object o,  Class<?> cls, String fileName, Object arg) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(fileName);
        field.setAccessible(true);
        field.set(o, arg);
    }


    /**
     * 两个数组合并
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

}
