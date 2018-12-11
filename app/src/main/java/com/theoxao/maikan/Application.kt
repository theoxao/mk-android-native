package com.theoxao.maikan

import android.app.Application
import android.content.Context
import com.theoxao.maikan.utils.AppUtils
import com.theoxao.maikan.utils.SharedPreferencesUtil

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        SharedPreferencesUtil.init(applicationContext, packageName + "_preference", Context.MODE_MULTI_PROCESS)
    }
}