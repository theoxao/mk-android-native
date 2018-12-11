package com.theoxao.maikan.utils

import java.util.*

class StringUtils {

    companion object {
        fun getStrList(target: String, length: Int): Vector<String> {
            var size = target.length / length
            if (target.length % length != 0) {
                size += 1
            }
            return getStrList(target, length, size)
        }

        private fun getStrList(target: String, length: Int, size: Int): Vector<String> {
            val list = Vector<String>()
            for (index: Int in 0 until size) {
                val childStr = substring(target, index * length, (index + 1) * length)
                list.add(childStr)
            }
            return list
        }

        private fun substring(str: String, f: Int, t: Int): String? {
            if (f > str.length)
                return null
            if (t > str.length) {
                return str.substring(f, str.length)
            } else {
                return str.substring(f, t)
            }
        }
    }

}