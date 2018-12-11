package com.theoxao.maikan.mvp

open interface MultiResultCallback {

    fun onSuccess(target: String, data: String)
    fun onFailure(target: String, msg: String)
    fun onNotFound(target: String)
    fun onUnAuthorized(target: String)
    fun onUnPurchased(target: String)
    fun onError()
    fun onComplete()

}