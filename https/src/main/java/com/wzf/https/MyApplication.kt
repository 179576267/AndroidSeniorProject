package com.wzf.https

import android.app.Application
import android.app.Dialog
import android.content.Context
import com.demo.base.i.OnShareSuccessListener
import com.demo.base.provider.BaseModuleProvider
import com.demo.base.provider.BaseProviderManager

class MyApplication : Application(){
    lateinit var  myApplication:Application
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }


}