package com.theoxao.maikan.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import com.ruffian.library.widget.REditText
import com.ruffian.library.widget.RTextView
import com.theoxao.maikan.R
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.utils.AppUtils
import com.theoxao.maikan.utils.PicassoImageLoader
import com.xys.libzxing.zxing.activity.CaptureActivity

class AddBookActivity : MultiResultActivity() {

    companion object {
        val FROM_KEY = "from"
        val FROM_SHELF = 0
        val FROM_SELECT = 1

        var that: AddBookActivity? = null
    }

    private lateinit var presenter: MultiPresenter
    private lateinit var scanEntry: RTextView
    private lateinit var coverView: ImageView
    private lateinit var nameView: REditText
    private lateinit var authorView: REditText
    private lateinit var publisherView: REditText
    private lateinit var pageCountView: REditText
    private lateinit var tagSelectEntry: RTextView
    private lateinit var readProgress: REditText
    private lateinit var borrowed: CheckBox
    private lateinit var alreadyRead: CheckBox
    private lateinit var reading: CheckBox
    private lateinit var returnDate: REditText
    private lateinit var remark: REditText
    private lateinit var slideIn: Animation
    private lateinit var slideOut: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        that = this
        scanEntry = findViewById(R.id.scan_isbn_entry)
        coverView = findViewById(R.id.book_cover)
        nameView = findViewById(R.id.name_edit_view)
        authorView = findViewById(R.id.author_edit_view)
        publisherView = findViewById(R.id.publisher_edit_view)
        pageCountView = findViewById(R.id.page_edit_view)
        tagSelectEntry = findViewById(R.id.tag_select_entry)
        readProgress = findViewById(R.id.read_progress)
        borrowed = findViewById(R.id.borrowed)
        returnDate = findViewById(R.id.return_date)
        remark = findViewById(R.id.remark)
        alreadyRead = findViewById(R.id.already_read)
        reading = findViewById(R.id.reading)
        slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in)
        slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out)

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
            }
        }
        tagSelectEntry.setOnClickListener {
            val intent = Intent(this, TagSelectActivity::class.java)
            intent.putExtra("selectTag", tagSelectEntry.text.toString())
            startActivityForResult(intent, 0)
        }

        reading.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alreadyRead.isChecked = false
                readProgress.startAnimation(slideIn)
                readProgress.visibility = View.VISIBLE
            } else {
                readProgress.startAnimation(slideOut)
                readProgress.visibility = View.INVISIBLE

                AppUtils.runOnUIDelayed({
                    readProgress.visibility = View.GONE
                }, 250L)
            }
        }
        alreadyRead.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                reading.isChecked = false
        }


        borrowed.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                returnDate.startAnimation(slideIn)
                returnDate.visibility = View.VISIBLE
                remark.startAnimation(slideIn)
                remark.visibility = View.VISIBLE
            } else {
                returnDate.startAnimation(slideOut)
                remark.startAnimation(slideOut)
                returnDate.visibility = View.INVISIBLE
                remark.visibility = View.INVISIBLE
                AppUtils.runOnUIDelayed({
                    returnDate.visibility = View.GONE
                    remark.visibility = View.GONE
                }, 250L)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val tag = intent?.extras?.getString("tag")
            tagSelectEntry.setText(tag)
        }
    }

    override fun onSuccess(target: String, data: String) {
    }

}
