package me.zwonb.wanandroid.network

import me.zwonb.wanandroid.BuildConfig
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.DefaultDomain
import java.util.concurrent.TimeUnit

fun initNetwork() {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    RxHttpPlugins.init(client)
        .setDebug(BuildConfig.DEBUG, true, 2)
}

@DefaultDomain
const val baseUrl = "https://www.wanandroid.com/"