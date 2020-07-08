package com.example.aspect.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TestAspect {

    public static final String TAG = "TestAspect";

    @Before("execution(* *..MainActivity.on*(..))")
    public void logLifeCycle(JoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getThis().getClass().getSimpleName();
        Log.e(TAG, "class:" + className+"   method:" + methodSignature.getName());

    }


    @Around("execution(* *..MainActivity.testAop())")
    public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        String key = proceedingJoinPoint.getSignature().toString();
        Log.e(TAG, "onActivityMethodAroundFirst: before " + key+"do something");
        //执行原方法
        proceedingJoinPoint.proceed();
        Log.e(TAG, "onActivityMethodAroundSecond: after " + key+"do something");

    }
}
