package me.zwonb.wanandroid.data

import me.zwonb.wanandroid.data.bean.BaseBean
import me.zwonb.wanandroid.data.bean.HomeBean
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    suspend fun homeData(page: Int = 0) = RxHttp.get("article/list/%d/json", page)
        .toAwait<BaseBean<HomeBean>>().await()
}