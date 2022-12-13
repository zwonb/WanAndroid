package me.zwonb.wanandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.zwonb.wanandroid.network.initNetwork

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initNetwork()
    }
}