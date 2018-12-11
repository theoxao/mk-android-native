package com.theoxao.maikan.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.theoxao.maikan.R
import com.theoxao.maikan.ui.views.LoadingView

abstract class BaseActivity : AppCompatActivity(), BaseView {


    private lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        loadingView = LoadingView(this, R.style.CustomDialog)
        loadingView.setCanceledOnTouchOutside(false)
        super.onCreate(savedInstanceState)
    }

    override fun onShowLoading() {
        if (loadingView == null) {
            loadingView.show()
        }
    }

    override fun onHideLoading() {
        if (loadingView != null) {
            loadingView.hide()
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    abstract override fun onSuccess(data: String)

    override fun onFailure(msg: String) {
        showToast(msg)
    }

    override fun onError() {
        showToast("程序出错了...")
    }

}