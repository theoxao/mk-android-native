package com.theoxao.maikan.ui.views

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.theoxao.maikan.R

class LoadingView : ProgressDialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.loading_layout)
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.alpha = 51f
        window.attributes = params
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun setMessage(message: CharSequence?) {
        super.setMessage(message)
    }
}