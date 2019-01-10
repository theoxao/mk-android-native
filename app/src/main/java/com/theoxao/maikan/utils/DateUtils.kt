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
            return (if (hour == 0L) "00" else if (hour > 10) hour else "0$hour").toString() + "小时" + (if (minute == 0L) "00" else if (minute > 10) minute else "0$minute") + "分" + if (second == 0L) "00" else if (second > 10) second else "0$second" + "秒"
        }
    }
}