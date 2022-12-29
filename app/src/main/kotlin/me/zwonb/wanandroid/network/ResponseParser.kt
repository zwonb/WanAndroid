package me.zwonb.wanandroid.network

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.TimeoutCancellationException
import me.zwonb.wanandroid.BuildConfig
import me.zwonb.wanandroid.data.bean.BaseBean
import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@Parser(name = "Response")
open class ResponseParser<T> : TypeParser<T> {

    protected constructor() : super()
    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: Response): T? {
        val result = response.convertTo<BaseBean<T>>(BaseBean::class, *types)
        if (result.errorCode != 0) {
            throw CodeException(result.errorCode, result.errorMsg, result.data)
        }
        return result.data
    }
}

class CodeException(val code: Int, val msg: String, val data: Any? = null) : IOException()

val Throwable.code: Int
    get() =
        when (this) {
            is CodeException -> code
//            is HttpStatusCodeException -> statusCode // Http状态码异常
            else -> -1
        }

val Throwable.msg: String
    get() = when (this) {
        is CodeException -> msg
        is UnknownHostException -> "当前无网络，请检查你的网络设置"
        is SocketTimeoutException, is TimeoutException, is TimeoutCancellationException -> "网络不稳定，请稍后再试"
        is ConnectException -> "网络不稳定，请稍后再试"
        is HttpStatusCodeException -> "请求失败，http:$statusCode"
        is JsonSyntaxException -> "数据解析失败，数据格式错误"
        else -> {
            if (BuildConfig.DEBUG) Log.e("zwonb", "可能奔溃啦", this)
            "请求失败，请稍后再试"
        }
    }
