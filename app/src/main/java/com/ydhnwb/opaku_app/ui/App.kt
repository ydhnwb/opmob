package com.ydhnwb.opaku_app.ui

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    private lateinit var analytic: FirebaseAnalytics
    override fun onCreate() {
        analytic = FirebaseAnalytics.getInstance(this@App)
        super.onCreate()
    }
}