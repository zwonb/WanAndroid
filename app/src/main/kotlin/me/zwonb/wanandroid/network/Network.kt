package me.zwonb.wanandroid.network

import me.zwonb.wanandroid.BuildConfig
import me.zwonb.wanandroid.MyApplication
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.cookie.CookieStore
import rxhttp.wrapper.cookie.ICookieJar
import java.io.File
import java.util.concurrent.TimeUnit

fun initNetwork(application: MyApplication) {
    val file = File(application.externalCacheDir, "cookie")
    val cookieStore = CookieStore(file)
    val client = OkHttpClient.Builder()
        .cookieJar(cookieStore)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    RxHttpPlugins.init(client)
        .setDebug(BuildConfig.DEBUG, true, 2)
}

fun hasCookie(): Boolean {
    val cookieJar = RxHttpPlugins.getOkHttpClient().cookieJar as ICookieJar
    val url = "https://www.wanandroid.com/".toHttpUrl()
    return cookieJar.loadCookie(url).any { it.name == "token_pass" }
}

@DefaultDomain
const val baseUrl = "https://www.wanandroid.com/"