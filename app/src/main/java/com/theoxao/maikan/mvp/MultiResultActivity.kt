package com.theoxao.maikan.mvp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.theoxao.maikan.R
import com.theoxao.maikan.ui.views.LoadingView

abstract class MultiResultActivity : AppCompatActivity(), MultiResultView {

    protected var mContext: Context? = null

    lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        this.mContext = this;
        loadingView = LoadingView(this, R.style.CustomDialog)
        loadingView.setCanceledOnTouchOutside(false)
        super.onCreate(savedInstanceState)
    }


    override fun onUnAuthorized(target: String) {
        //TODO jump to log activity
    }


    abstract override fun onSuccess(target: String, data: String)

    override fun onFailure(target: String, msg: String) {
        showToast(msg)
    }

    override fun onShowLoading() {
        loadingView.show()
    }

    override fun onHideLoading() {
        loadingView.hide()
    }

    override fun onError() {
        showToast("发生错误了...")
    }

    override fun showToast(msg: String) {
        mContext?.let { Toast.makeText(it, msg, Toast.LENGTH_LONG).show() }
    }

}