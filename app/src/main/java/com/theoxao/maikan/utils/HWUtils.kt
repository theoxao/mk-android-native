package com.theoxao.maikan.utils

class HWUtils {
    companion object {
        fun hasNotch(): Boolean {
            try {
                val cl = AppUtils.getAppContext().classLoader
                val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
                val method = hwNotchSizeUtil.getMethod("hasNotchInScreen")
                return method.invoke(hwNotchSizeUtil) as Boolean
            } catch (e: Exception) {
                return false
            }
        }
    }
}