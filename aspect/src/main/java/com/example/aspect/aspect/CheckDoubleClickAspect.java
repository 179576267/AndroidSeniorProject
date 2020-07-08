package com.example.aspect.aspect;


import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Calendar;

@Aspect
public class CheckDoubleClickAspect {
    private static final String TAG = "TestAspect";
    /**
     * 最近一次点击的时间
     */
    private long mLastTime;
    /**
     * 最近一次点击的控件ID
     */
    private int mLastId;
    /**
     * 切入类型	                                    切入标识
     * Method execution	                    execution(MethodSignature)
     * Method call	                        call(MethodSignature)
     * Constructor execution	             execution(ConstructorSignature)
     * Constructor call	                    call(ConstructorSignature)
     * Class initialization	                staticinitialization(TypeSignature)
     * Field read access	                get(FieldSignature)
     * Field write access	                set(FieldSignature)
     * Exception handler execution  	    handler(TypeSignature)
     * Object initialization	            initialization(ConstructorSignature)
     * Object pre-initialization    	    preinitialization(ConstructorSignature)
     * Advice execution                  	adviceexecution()
     *
     * **：表示是任意包名
     * ..：表示任意类型任意多个参数
     */
//    @Pointcut("execution(@com.example.aspect.aspect.CheckDoubleClick  * *(..))")
    @Pointcut("@annotation(com.example.aspect.aspect.CheckDoubleClick)")
    public void method(){
        Log.i(TAG, "executionDoubleClick");
    }


    /**
     * 类型	                                 描述
     * Before	                前置通知, 在目标执行之前执行通知
     * After	                后置通知, 目标执行后执行通知
     * Around	                环绕通知, 在目标执行中执行通知, 控制目标执行时机
     * AfterReturning	        后置返回通知, 目标返回时执行通知
     * AfterThrowing	        异常通知, 目标抛出异常时执行通知
     */
//    @Around("method() && @annotation(CheckDoubleClick)")
//    @Around("execution(@com.example.aspect.aspect.CheckDoubleClick  * *(..))")

    @Around("method()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG, "around");
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if(arg instanceof View){
                view = (View) arg;
            }
        }

        if(view != null){
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - mLastTime < 2000 && view.getId()
                    == mLastId) {
                Log.i(TAG, "发生快速点击");
                return;
            }
            mLastTime = currentTime;
            mLastId = view.getId();
            //执行原方法
            joinPoint.proceed();
        }
    }

    @Before("method()")
    public void before(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        CheckDoubleClick click = signature.getMethod().getAnnotation(CheckDoubleClick.class);
        Log.i(TAG, "before : " + click.value());
    }

    @After("method()")
    public void after(JoinPoint joinPoint) throws Throwable {
        Log.i(TAG, "after");
    }

}
