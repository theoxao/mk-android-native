package com.theoxao.maikan.web

import android.util.Log.d
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.mvp.BaseCallback
import com.theoxao.maikan.mvp.MultiResultCallback
import com.theoxao.maikan.utils.AppUtils
import com.theoxao.maikan.utils.CacheUtil
import com.theoxao.maikan.utils.ObjectMapperUtils
import okhttp3.*
import java.io.IOException


class HttpModel {
    private var CLIENT = OkHttpClient.Builder().build()
    private var TOKEN = "70843bc3-794a-4d99-a05a-c4e6487036bd"

    fun getAnonymous(url: String, baseCallback: BaseCallback) {
        d("request {} now", url)
        val request = requestBuilder(url).build()
        CLIENT.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                AppUtils.runOnUI {
                    baseCallback.onError()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val node = ObjectMapperUtils.objectMapper.readTree(response?.body()!!.string())
                val status = node.get("status").asInt()
                when (status) {
                    200 -> {
                        AppUtils.runOnUI { baseCallback.onSuccess(node.get("data").toString()) }
                    }
                    500 -> AppUtils.runOnUI { baseCallback.onFailure("服务器内部出错...") }
                    else -> AppUtils.runOnUI { baseCallback.onFailure("出错啦...........啊") }
                }
            }
        })
    }

    @Deprecated("unused")
    fun getAuthorize(url: String, callback: BaseCallback) {
        val request = requestBuilder(url).build()
        CLIENT.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                AppUtils.runOnUI { callback.onError() }
            }
            override fun onResponse(call: Call?, response: Response?) {
                try {
                    val node = ObjectMapperUtils.objectMapper.readTree(response?.body()!!.string())
                    val status = node.get("status").asInt()
                    when (status) {
                        200 -> {
                            AppUtils.runOnUI { callback.onSuccess(node.get("data").toString()) }
                        }
                        500 -> AppUtils.runOnUI { callback.onFailure("服务器内部出错了") }
                        401 -> AppUtils.runOnUI { callback.onUnAuthorized() }
                        402 -> AppUtils.runOnUI { callback.onUnPurchased() }
                        else -> {
                            AppUtils.runOnUI { callback.onFailure("请求出错啦...请稍后再试哈") }
                        }
                    }
                } catch (e: Exception) {
                    AppUtils.runOnUI { e.message?.let { callback.onFailure(it) } }
                }

            }

        })

    }

    fun getWithUrl(url: String, target: String?, callback: MultiResultCallback) {
        d("request {} now ", url)
        val request = requestBuilder(url).build()
        CLIENT.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                AppUtils.runOnUI { callback.onError() }
                AppUtils.runOnUI { callback.onComplete() }
            }
            override fun onResponse(call: Call?, response: Response?) {
                try {
                    val node = ObjectMapperUtils.objectMapper.readTree(response?.body()!!.string())
                    val status = node.get("status").asInt()
                    when (status) {
                        200 -> AppUtils.runOnUI {
                            callback.onSuccess(target ?: url, node.get("data").toString())
                        }
                        500 -> AppUtils.runOnUI {
                            if (node.get("error") != null)
                                callback.onFailure(target ?: url, node.get("error").asText())
                            else
                                callback.onFailure(target ?: url, "服务器内部出错了")
                        }
                        401 -> AppUtils.runOnUI { callback.onUnAuthorized(target ?: url) }
                        402 -> AppUtils.runOnUI { callback.onUnPurchased(target ?: url) }
                        404 -> AppUtils.runOnUI { callback.onNotFound(target ?: url) }
                        else -> {
                            AppUtils.runOnUI { callback.onFailure(target ?: url, "请求出错啦...请稍后再试哈") }
                        }
                    }
                    AppUtils.runOnUI { callback.onComplete() }
                } catch (e: Exception) {
                    AppUtils.runOnUI { e.message?.let { callback.onFailure(target ?: url, it) } }
                }

            }
        })
    }

    fun postDataAsync(url: String, params: Map<String, String>?, target: String?, callback: MultiResultCallback) {
        d("posting to  {} now ", url)
        val request = requestBuilder(url).post(setRequestBody(params)).build()
        CLIENT.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                AppUtils.runOnUI { callback.onError() }
                AppUtils.runOnUI { callback.onComplete() }
            }
            override fun onResponse(call: Call?, response: Response?) {
                try {
                    val node = ObjectMapperUtils.objectMapper.readTree(response?.body()!!.string())
                    val status = node.get("status").asInt()
                    when (status) {
                        200 -> AppUtils.runOnUI {
                            callback.onSuccess(target ?: url, node.get("data").toString())
                        }
                        500 -> AppUtils.runOnUI {
                            if (node.get("error") != null)
                                callback.onFailure(target ?: url, node.get("error").asText())
                            else callback.onFailure(target ?: url, "服务器内部出错了")
                        }
                        401 -> AppUtils.runOnUI { callback.onUnAuthorized(target ?: url) }
                        402 -> AppUtils.runOnUI { callback.onUnPurchased(target ?: url) }
                        404 -> AppUtils.runOnUI { callback.onNotFound(target ?: url) }
                        else -> {
                            AppUtils.runOnUI { callback.onFailure(target ?: url, "请求出错啦...请稍后再试哈") }
                        }
                    }
                    AppUtils.runOnUI { callback.onComplete() }
                } catch (e: Exception) {
                    AppUtils.runOnUI { e.message?.let { callback.onFailure(target ?: url, it) } }
                }

            }
        })
    }

    private fun requestBuilder(url: String): Request.Builder {
        return Request.Builder().addHeader("token", TOKEN).url("${Routers.host}$url")
    }

    private fun setRequestBody(BodyParams: Map<String, String>?): RequestBody {
        val formEncodingBuilder = okhttp3.FormBody.Builder()
        if (BodyParams != null) {
            val iterator = BodyParams.keys.iterator()
            var key: String
            while (iterator.hasNext()) {
                key = iterator.next()
                formEncodingBuilder.add(key, BodyParams[key]!!)
                d("post http", "post_Params===" + key + "====" + BodyParams[key])
            }
        }
        return formEncodingBuilder.build()
    }

    private fun getType(url: String): Int {
        return 0
    }
}