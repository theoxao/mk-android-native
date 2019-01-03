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
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.xys.libzxing.zxing.activity.CaptureActivity

class AddBookActivity : MultiResultActivity() {
    private lateinit var presenter: MultiPresenter
    private lateinit var scanEntry: RTextView
    private lateinit var coverView: ImageView
    private lateinit var nameView: REditText
    private lateinit var authorView: REditText
    private lateinit var publisherView: REditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        scanEntry = findViewById(R.id.scan_isbn_entry)

        presenter = MultiPresenter(this)
        presenter.requestData(Routers.TAGLIST, null)
        addEntry.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            2)
                } else {
                    showToast("权限已申请")
                }
                val openCameraIntent = Intent(this, CaptureActivity::class.java)
                startActivityForResult(openCameraIntent, 0)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null)
                return
            val isbn = data.extras.getString("result")
            presenter.requestData(Routers.ISBN_SEARCH + isbn, "isbn")
        }
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
