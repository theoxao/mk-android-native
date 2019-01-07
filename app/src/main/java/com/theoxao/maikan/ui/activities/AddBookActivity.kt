package com.theoxao.maikan.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.ruffian.library.widget.REditText
import com.ruffian.library.widget.RTextView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.functions.ErrorTextWatcher
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.utils.AppUtils
import com.theoxao.maikan.utils.PicassoImageLoader
import com.theoxao.maikan.utils.TimeSyncHelper
import com.theoxao.maikan.utils.ToastUtils
import com.xys.libzxing.zxing.activity.CaptureActivity
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddBookActivity : MultiResultActivity() {

    companion object {
        val FROM_KEY = "from"
        val FROM_SHELF = 0
        val FROM_SELECT = 1
        val CAMERA_REQUEST = 10
        val GALLERY_REQUEST = 11
        @SuppressLint("StaticFieldLeak")
        var that: AddBookActivity? = null
    }

    private var coverUrl: String? = null
    private var isbn: String? = null
    private var refBookId: String? = null
    private var currentImagePath: String? = null
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
    private lateinit var shakeAnim: Animation
    private lateinit var addBookSubmit: RTextView
    private lateinit var requestBody: HashMap<String, String>
    private var nonBlankParams = arrayListOf<REditText>()
    private lateinit var rxPermissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        that = this
        scanEntry = findViewById(R.id.scan_isbn_entry)
        coverView = findViewById(R.id.book_cover)
        nameView = findViewById(R.id.name_edit_view)
        nonBlankParams.add(nameView)
        authorView = findViewById(R.id.author_edit_view)
        nonBlankParams.add(authorView)
        publisherView = findViewById(R.id.publisher_edit_view)
        pageCountView = findViewById(R.id.page_edit_view)
        nonBlankParams.add(pageCountView)
        tagSelectEntry = findViewById(R.id.tag_select_entry)
        readProgress = findViewById(R.id.read_progress)
        borrowed = findViewById(R.id.borrowed)
        returnDate = findViewById(R.id.return_date)
        remark = findViewById(R.id.remark)
        alreadyRead = findViewById(R.id.already_read)
        reading = findViewById(R.id.reading)
        addBookSubmit = findViewById(R.id.add_book_submit)
        slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in)
        slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out)
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)
        presenter = MultiPresenter(this)
        rxPermissions = RxPermissions(this)

        val from = intent.getIntExtra(FROM_KEY, FROM_SHELF)
        if (FROM_SELECT == from) {
            val data = intent.data
            coverUrl = data.toString()
            isbn = intent.getStringExtra("isbn")
            refBookId = intent.getStringExtra("refBookId")
            PicassoImageLoader().displayCover(this, coverUrl, coverView, intent.getStringExtra("name"))
            nameView.setText(intent.getStringExtra("name"))
            authorView.setText(intent.getStringExtra("author"))
            publisherView.setText(intent.getStringExtra("publisher"))
            pageCountView.setText(intent.getStringExtra("pageCount"))
            scanEntry.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_top))
            scanEntry.visibility = View.VISIBLE
        } else scanEntry.visibility = View.VISIBLE

        scanEntry.setOnClickListener { _ ->
            rxPermissions.request(Manifest.permission.CAMERA).subscribe {
                if (it) {
                    val openCameraIntent = Intent(this, CaptureActivity::class.java)
                    startActivity(openCameraIntent)
                } else {
                    ToastUtils.showLongToast("该功能需要相机权限")
                }
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
        //ENHANCE editText回显dialog
        val calendar = TimeSyncHelper.readCalendar()
        d(this.localClassName, calendar.timeInMillis.toString())
        val dpd = DatePickerDialog(this@AddBookActivity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            returnDate.setText("$year-${month + 1}-$dayOfMonth")
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dpd.datePicker.maxDate = calendar.timeInMillis + 10368000000L
        dpd.datePicker.minDate = calendar.timeInMillis + 86400000L
        returnDate.setOnClickListener {
            dpd.show()
        }

        borrowed.setOnCheckedChangeListener { _, isChecked ->
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

        coverView.setOnClickListener {
            showPopView()
        }

        val danger = ContextCompat.getColor(this@AddBookActivity, R.color.danger)
        readProgress.helper.setBorderColor(danger, danger, danger, danger)
        readProgress.addTextChangedListener(object : ErrorTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                readProgress.helper.setBorderWidth(0, 0, 0, 0)
            }
        })
        returnDate.helper.setBorderColor(danger, danger, danger, danger)
        returnDate.addTextChangedListener(object : ErrorTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                returnDate.helper.setBorderWidth(0, 0, 0, 0)
            }
        })
        nonBlankParams.forEach {
            val helper = it.helper
            helper.setBorderColor(danger, danger, danger, danger)
            it.addTextChangedListener(object : ErrorTextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    helper.setBorderWidth(0, 0, 0, 0)
                }
            })
        }
        addBookSubmit.setOnClickListener { _ ->
            var flag = true
            nonBlankParams.forEach {
                if (StringUtils.isBlank(it.text.toString())) {
                    flag = false
                    val helper = it.helper
                    helper.setBorderWidth(2, 2, 2, 2)
                    it.startAnimation(shakeAnim)
                }
            }
            if (reading.isChecked && StringUtils.isBlank(readProgress.text.toString())) {
                flag = false
                readProgress.helper.setBorderWidth(2, 2, 2, 2)
                readProgress.startAnimation(shakeAnim)
            }
            if (borrowed.isChecked && StringUtils.isBlank(returnDate.text.toString())) {
                flag = false
                returnDate.helper.setBorderWidth(2, 2, 2, 2)
                returnDate.startAnimation(shakeAnim)
            }
            if (flag) {
                prepareRequestBody()
                presenter.postData(Routers.ADD_BOOK, requestBody, Routers.ADD_BOOK)
            }
        }
    }

    private fun showPopView() {
        val popView = View.inflate(this, R.layout.upload_cover_pop_view, null)
        val fromCamera = popView.findViewById<TextView>(R.id.from_camera)
        val fromGallery = popView.findViewById<TextView>(R.id.from_gallery)
        val cancel = popView.findViewById<TextView>(R.id.cancel)

        val attributes = window.attributes

        val popupWindow = PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        popupWindow.elevation = 8f

        attributes.alpha = 0.5f
        window.attributes = attributes
        fromCamera.setOnClickListener { _ ->
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
                if (it)
                    takeCamera()
                else
                    ToastUtils.showLongToast("该功能需要相机和存储权限")
            }
        }
        fromGallery.setOnClickListener {
            println("fromGallery")
        }
        cancel.setOnClickListener {
            popupWindow.dismiss()
        }
        popupWindow.setOnDismissListener {
            attributes.alpha = 1f
            window.attributes = attributes
        }

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0)
    }

    private fun takeCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(this.packageManager) != null) {
            val file: File? = createImageFile()
            if (file != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
            }
        }
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    private fun createImageFile(): File? {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        var file: File? = null
        try {
            file = File.createTempFile("JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_", ".jpg", dir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        currentImagePath = file?.absolutePath
        return file
    }

    private fun prepareRequestBody() {
        requestBody = HashMap()
        coverUrl?.let { requestBody["cover"] = it }
        isbn?.let { requestBody["isbn"] = it }
        requestBody["author"] = authorView.text.toString()
        requestBody["publisher"] = publisherView.text.toString()
        requestBody["pageCount"] = pageCountView.text.toString()
        requestBody["tag"] = tagSelectEntry.text.toString()
        requestBody["name"] = nameView.text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                0 -> {
                    val tag = data?.extras?.getString("tag")
                    tagSelectEntry.text = tag
                }
                GALLERY_REQUEST -> {
                    val uri = data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    uploadPic(picturePath)
                    cursor.close()
                }
            }
        }
    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            Routers.ADD_BOOK -> {
                ToastUtils.showLongToast("添加成功")
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

}
