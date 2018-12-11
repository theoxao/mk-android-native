package com.theoxao.maikan.mvp

open interface MultiResultView : BaseView {
    override fun onShowLoading()
    override fun onHideLoading()
    override fun showToast(msg: String)
    fun onSuccess(target: String, data: String)
    fun onFailure(target: String, msg: String)
    override fun onSuccess(data: String) {
        //do nothing
    }

    override fun onFailure(msg: String) {
        //do nothing
    }

    override fun onError()

    fun onNotFound(target: String) {

    }

    fun onUnAuthorized(target: String) {
    }

    fun onUnPurchased(target: String) {}
}