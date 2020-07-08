package com.wzf.base.arouter.router;

import java.util.LinkedHashMap;

/**
 * @author wangzhanghuan
 * @version 1.0
 * @date create 2019/6/11
 * @Description
 * @Project woshimi_dev
 * @Package com.wzf.basedata.utils.router
 * @Copyright personal
 */

public class RouterMap extends LinkedHashMap<String, String> {

    public boolean hasValue(){
        return !isEmpty();
    }

    public int getInt(String key, int def) {

        String s = get(key);
        if (s != null) {
            try {
                return Integer.valueOf(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return def;
    }

    public long getLong(String key, long def) {

        String s = get(key);
        if (s != null) {
            try {
                return Long.valueOf(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return def;
    }

    public float getFloat(String key, float def) {

        String s = get(key);
        if (s != null) {
            try {
                return Float.valueOf(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return def;
    }

    public double getDouble(String key, double def) {

        String s = get(key);
        if (s != null) {
            try {
                return Double.valueOf(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return def;
    }

    public boolean getBool(String key, boolean def) {

        String s = get(key);
        if (s != null) {
            return ("1".equals(s) || "true".equalsIgnoreCase(s));
        }
        return def;
    }

    public String[] getStringArray(String key) {

        String s = get(key);
        if (s != null && s.contains(",")) {
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace("{", "");
            s = s.replace("}", "");
            return s.split(",");

        }
        return null;
    }


}
