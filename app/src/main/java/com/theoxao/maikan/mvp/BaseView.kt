package com.theoxao.maikan.mvp

open interface BaseView {

    fun onShowLoading()
    fun onHideLoading()

    fun showToast(msg: String)

    fun onSuccess(data: String)
    fun onFailure(msg: String)
    fun onError()

}