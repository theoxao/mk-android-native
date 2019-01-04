package com.theoxao.maikan.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity

class TagSelectActivity : MultiResultActivity() {

    private lateinit var presenter: MultiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_select)
        presenter = MultiPresenter(this)
        presenter.requestData(Routers.TAGLIST, null)

    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            Routers.TAGLIST -> {

            }
        }
    }
}
