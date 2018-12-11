package com.theoxao.maikan.mvp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.theoxao.maikan.R
import com.theoxao.maikan.ui.views.LoadingView

abstract class MultiResultFragment : Fragment(), MultiResultView {

    protected var mContext: Context? = null
    protected var mRootView: View? = null

    private lateinit var loadingView: LoadingView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.mContext = activity;
        loadingView = LoadingView(activity, R.style.CustomDialog)
        loadingView.setCanceledOnTouchOutside(false)
        return mRootView;
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