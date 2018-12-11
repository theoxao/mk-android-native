package com.theoxao.maikan.mvp

import com.theoxao.maikan.utils.WebUtils
import com.theoxao.maikan.web.HttpModel


class MultiPresenter(override var mView: MultiResultView?) : BasePresenter<MultiResultView>(mView) {

    override fun requestData(url: String, target: String?) {
        mView?.onShowLoading()
        HttpModel().getWithUrl(url, target, object : MultiResultCallback {
            override fun onNotFound(target: String) {
                mView?.onNotFound(target)
            }

            override fun onSuccess(target: String, data: String) {
                mView?.onSuccess(target, data)
            }

            override fun onFailure(target: String, msg: String) {
                mView?.onFailure(target, msg)
            }

            override fun onUnAuthorized(target: String) {
                mView?.onUnAuthorized(target)
            }

            override fun onUnPurchased(target: String) {
                mView?.onUnPurchased(target)
            }

            override fun onError() {
            }

            override fun onComplete() {
                mView?.onHideLoading()
            }
        })
    }

    fun getData(url: String, params: Map<String, Any>, target: String?) {
        requestData(WebUtils.appendQueryParameters(url, params), target)
    }

    fun postData(url: String, params: Map<String, String>?, target: String?) {
        mView?.onShowLoading()
        HttpModel().postDataAsync(url, params, target, object : MultiResultCallback {
            override fun onNotFound(target: String) {
                mView?.onNotFound(target)
            }

            override fun onSuccess(target: String, data: String) {
                mView?.onSuccess(target, data)
            }

            override fun onFailure(target: String, msg: String) {
                mView?.onFailure(target, msg)
            }

            override fun onUnAuthorized(target: String) {
                mView?.onUnAuthorized(target)
            }

            override fun onUnPurchased(target: String) {
                mView?.onUnPurchased(target)
            }

            override fun onError() {
                mView?.onError()
            }

            override fun onComplete() {
                mView?.onHideLoading()
            }

        })
    }
}