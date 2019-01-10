package com.theoxao.maikan.utils

import android.support.v4.content.ContextCompat
import com.theoxao.maikan.R

class ColorUtils {

    companion object {
        val colorPrimary: Int = ContextCompat.getColor(AppUtils.getAppContext(), R.color.colorPrimary)
        val colorLight: Int = ContextCompat.getColor(AppUtils.getAppContext(), R.color.light)
    }

}