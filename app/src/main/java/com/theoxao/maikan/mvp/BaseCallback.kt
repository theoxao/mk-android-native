package com.theoxao.maikan.mvp


open interface BaseCallback {
    fun onSuccess(data: String)
    fun onFailure(msg: String)
    fun onUnAuthorized()
    fun onUnPurchased()
    fun onError()
    fun onComplete()
}