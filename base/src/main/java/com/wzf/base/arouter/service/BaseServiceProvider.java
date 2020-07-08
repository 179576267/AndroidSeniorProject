package com.wzf.base.arouter.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.demo.base.utils.ActivityCollector;

import java.io.Serializable;

import io.reactivex.functions.Function;

public abstract class BaseServiceProvider<R> implements IProvider, Function<Bundle, R> {
    public abstract R apply(Bundle bundle);

    @Override
    public void init(Context context) {

    }

    Bundle bundle;

    public BaseServiceProvider apply() {
        checkBundle();
        apply(bundle);
        return this;
    }

    private void checkBundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
    }

    public BaseServiceProvider withBoolean(String key, boolean value) {
        checkBundle();
        bundle.putBoolean(key, value);
        return this;
    }


    public BaseServiceProvider withInt(String key, int value) {
        checkBundle();
        bundle.putInt(key, value);
        return this;
    }

    public BaseServiceProvider withLong(String key, long value) {
        checkBundle();
        bundle.putLong(key, value);
        return this;
    }

    public BaseServiceProvider withString(String key, String value) {
        checkBundle();
        bundle.putString(key, value);
        return this;
    }

    public BaseServiceProvider withSerializable(String key, Serializable value) {
        checkBundle();
        bundle.putSerializable(key, value);
        return this;
    }

    public Activity getBaseActi(){
        return ActivityCollector.getCurrentActivity();

    }


}
