package com.theoxao.maikan.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class WebUtils {

    companion object {
        fun appendQueryParameters(url: String, params: Map<String, Any>): String {
            if (params.isEmpty()) {
                return url
            }
            val queryString = toQueryString(params)
            val anchorPos = url.indexOf('#')
            var main = url
            var hasAnchor = false
            if (anchorPos != -1) {
                main = url.substring(0, anchorPos)
                hasAnchor = true
            }
            val markPos = main.indexOf('?')
            if (markPos == -1) {
                val result = StringBuilder()
                result.append(main).append('?').append(queryString)
                if (hasAnchor) {
                    result.append(url.substring(anchorPos))
                }
                return result.toString()
            } else {
                val result = StringBuilder()
                result.append(main)
                val lastChar = main[main.length - 1]
                if (lastChar != '&' && lastChar != '?') {
                    result.append('&')
                }
                result.append(queryString)
                if (hasAnchor) {
                    result.append(url.substring(anchorPos))
                }
                return result.toString()
            }
        }


        private fun toQueryString(params: Map<String, Any>): String {
            val result = StringBuilder()
            for ((key, value) in params) {
                result.append(key).append('=').append(urlEncode(value.toString(), "UTF-8")).append('&')
            }
            if (result.length > 0) {
                result.setLength(result.length - 1)
            }
            return result.toString()
        }

        private fun urlEncode(value: String, charset: String): String {
            try {
                return URLEncoder.encode(value, charset)
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }

        }

    }
}