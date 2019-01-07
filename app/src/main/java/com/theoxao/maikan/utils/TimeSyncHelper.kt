package com.theoxao.maikan.utils

import android.os.SystemClock
import java.util.*

class TimeSyncHelper {
    companion object {
        var serverTimestamp: Long = System.currentTimeMillis()
        var systemTimestamp: Long = System.currentTimeMillis()
        fun syncServer(timestamp: Long) {
            serverTimestamp = timestamp
            systemTimestamp = SystemClock.elapsedRealtime()
        }

        fun realTimestamp(): Long {
            return serverTimestamp + SystemClock.elapsedRealtime() - systemTimestamp
        }

        fun readDate(): Date {
            return Date(realTimestamp())
        }

        fun readCalendar(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = realTimestamp()
            return calendar
        }
    }
}