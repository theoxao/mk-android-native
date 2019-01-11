package com.theoxao.maikan.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun parseDate(date: Date): String {
            val sdf = SimpleDateFormat("yyyy年MM月dd日")
            return sdf.format(date)
        }

        fun parseDuration(duration: Long): String {
            return format(duration)
        }

        private fun format(time: Long): String {
            val hour = time / (60 * 60 * 1000)
            val minute = (time - hour * 60 * 60 * 1000) / (60 * 1000)
            val second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000
            return (if (hour == 0L) "" else "${hour}小时") + (if (minute == 0L) "" else "${minute}分钟") + "${second}秒"
        }
    }
}