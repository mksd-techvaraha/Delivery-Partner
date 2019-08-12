package com.sunthemart.deliverypartner.view.ui

import android.app.Application
import android.util.Log
import com.sunthemart.deliverypartner.utils.SharedPreferenceInstance

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("MyApplication", "The application has lunched")
        SharedPreferenceInstance.init(applicationContext)

    }
}