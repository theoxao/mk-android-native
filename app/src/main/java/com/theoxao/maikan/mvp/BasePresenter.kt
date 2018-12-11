package com.theoxao.maikan.mvp

import android.os.Handler
import com.theoxao.maikan.web.HttpModel

open class BasePresenter<V : BaseView>(open var mView: V?) {


    fun attachView(mView: V) {
        this.mView = mView
    }

    fun detachView() {
        this.mView = null
    }

    fun isViewAttached() {
        this.mView != null
    }

    open fun requestData(url: String, target: String?) {

        HttpModel().getAnonymous(url, object : BaseCallback {
            override fun onUnAuthorized() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onUnPurchased() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuccess(data: String) {
                mView?.onSuccess(data)
            }

            override fun onFailure(msg: String) {
                mView?.onFailure(msg)
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