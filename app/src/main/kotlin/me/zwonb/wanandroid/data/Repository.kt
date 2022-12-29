package me.zwonb.wanandroid.data

import me.zwonb.wanandroid.data.bean.BannerBean
import me.zwonb.wanandroid.data.bean.BaseBean
import me.zwonb.wanandroid.data.bean.HomeBean
import rxhttp.toAwait
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toFlowResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    suspend fun homeData(page: Int = 0) = RxHttp.get("article/list/%d/json", page)
        .toAwait<BaseBean<HomeBean>>().await()

    fun banner() = RxHttp.get("banner/json").toFlow<BaseBean<List<BannerBean>>>()

    fun collect(id: Int) = RxHttp.postForm("lg/collect/$id/json")
        .toFlowResponse<Any?>()

    fun login(username: String,password: String) = RxHttp.postForm("user/login")
        .add("username", username)
        .add("password", password)
        .toFlowResponse<Any?>()
}