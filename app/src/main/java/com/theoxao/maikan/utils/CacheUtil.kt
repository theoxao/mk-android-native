package com.theoxao.maikan.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import org.json.JSONObject
import java.io.Serializable

class CacheUtil {

    companion object {
        val instance: CacheUtil by lazy { CacheUtil() }
    }

    private val cache: ACache = ACache.get(AppUtils.getAppContext())

    private fun put(value: Any, key: String, duration: Int) {
        when (value) {
            is String -> cache.put(key, value, duration)
            is Bitmap -> cache.put(key, value, duration)
            is Drawable -> cache.put(key, value, duration)
            is JSONObject -> cache.put(key, value, duration)
            is Serializable -> cache.put(key, value, duration)
        }
    }

    fun get(key: String): String? {
        return cache.getAsString(key)
    }

    fun put(key: String, value: Any, type: Int) {
        val duration: Int = when (type) {
            1 -> ACache.TIME_HOUR
            2 -> ACache.TIME_HOUR * 3
            3 -> ACache.TIME_HOUR * 6
            4 -> ACache.TIME_DAY
            5 -> ACache.TIME_DAY * 10
            else -> -1
        }
        if (duration != -1) {
            this.put(value, key, duration)
        }
    }

//    /**
//     * 一小时
//     */
//    fun putShort(key: String, value: Any) {
//        put(key, value, ACache.TIME_HOUR)
//    }
//
//    /**
//     * 3小时
//     */
//    fun putMiddle(key: String, value: Any) {
//        put(key, value, ACache.TIME_HOUR * 3)
//    }
//
//    /**
//     * 6小时
//     */
//    fun putLong(key: String, value: Any) {
//        put(key, value, ACache.TIME_HOUR * 6)
//    }
//
//    /**
//     * 一天
//     */
//    fun putDay(key: String, value: Any) {
//        put(key, value, ACache.TIME_DAY)
//    }
//
//    /**
//     * 十天
//     */
//    fun putTenDay(key: String, value: Any) {
//        put(key, value, ACache.TIME_DAY * 10)
//    }
}