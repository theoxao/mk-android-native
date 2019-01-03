package com.theoxao.maikan.ui.activities

import android.os.Bundle
import android.util.Log.d
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity

class AddBookActivity : MultiResultActivity() {
    private lateinit var presenter: MultiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        val isbn = intent.getStringExtra("isbn")
        presenter = MultiPresenter(this)
        presenter.requestData(Routers.ISBN_SEARCH + isbn, "isbn")
        presenter.requestData(Routers.TAGLIST, null)
    }

    override fun onSuccess(target: String, data: String) {
        d(this.localClassName, data)
        when (target) {
            "isbn" -> {

            }
            Routers.TAGLIST -> {

            }
        }
    }
}
