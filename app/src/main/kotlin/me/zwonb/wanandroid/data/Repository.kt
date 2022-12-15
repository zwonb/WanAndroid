package me.zwonb.wanandroid.data

import com.google.gson.JsonObject
import me.zwonb.wanandroid.data.bean.BannerBean
import me.zwonb.wanandroid.data.bean.BaseBean
import me.zwonb.wanandroid.data.bean.HomeBean
import rxhttp.toAwait
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    suspend fun homeData(page: Int = 0) = RxHttp.get("article/list/%d/json", page)
        .toAwait<BaseBean<HomeBean>>().await()

    fun banner() = RxHttp.get("banner/json").toFlow<BaseBean<List<BannerBean>>>()

    fun collect(id: Int) = RxHttp.postForm("lg/collect/$id/json")
        .addHeader("")
        .toFlow<BaseBean<JsonObject>>()
}