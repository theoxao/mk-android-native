package com.theoxao.maikan.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.ruffian.library.widget.REditText
import com.ruffian.library.widget.RTextView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.utils.PicassoImageLoader
import com.xys.libzxing.zxing.activity.CaptureActivity

class AddBookActivity : MultiResultActivity() {

    companion object {
        val FROM_KEY = "from"
        val FROM_SHELF = 0
        val FROM_SELECT = 1
    }

    private lateinit var presenter: MultiPresenter
    private lateinit var scanEntry: RTextView
    private lateinit var coverView: ImageView
    private lateinit var nameView: REditText
    private lateinit var authorView: REditText
    private lateinit var publisherView: REditText
    private lateinit var pageCountView: REditText
    private lateinit var tagSelectEntry: RTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        scanEntry = findViewById(R.id.scan_isbn_entry)
        coverView = findViewById(R.id.book_cover)
        nameView = findViewById(R.id.name_edit_view)
        authorView = findViewById(R.id.author_edit_view)
        publisherView = findViewById(R.id.publisher_edit_view)
        pageCountView = findViewById(R.id.page_edit_view)

        val from = intent.getIntExtra(FROM_KEY, FROM_SHELF)
        if (FROM_SELECT == from) {
            val data = intent.data
            PicassoImageLoader().displayImage(this, intent.data.toString(), coverView)
            nameView.setText(intent.getStringExtra("name"))
            authorView.setText(intent.getStringExtra("author"))
            publisherView.setText(intent.getStringExtra("publisher"))
            pageCountView.setText(intent.getStringExtra("pageCount"))
            scanEntry.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_top))
            scanEntry.visibility = View.VISIBLE
        } else scanEntry.visibility = View.VISIBLE

        presenter = MultiPresenter(this)
        presenter.requestData(Routers.TAGLIST, null)
        scanEntry.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            2)
                } else {
                    showToast("权限已申请")
                }
                val openCameraIntent = Intent(this, CaptureActivity::class.java)
                startActivity(openCameraIntent)
                this.finish()
            }
        }
        tagSelectEntry.setOnClickListener {
            startActivityForResult(Intent(this, TagSelectActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val tag = intent.getStringExtra("tag")
            tagSelectEntry.setText(tag)
        }
    }

    override fun onSuccess(target: String, data: String) {
        d(this.localClassName, data)
        when (target) {
            Routers.TAGLIST -> {

            }
        }
    }

}
