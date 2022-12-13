package me.zwonb.wanandroid.data

import me.zwonb.wanandroid.data.bean.BaseBean
import org.json.JSONObject
import rxhttp.toFlow
import rxhttp.wrapper.param.RxHttp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    fun homeData(page: Int = 0) = RxHttp.get("").toFlow<BaseBean<JSONObject>>()
}