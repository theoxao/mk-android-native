package com.theoxao.maikan.ui.fragments


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.theoxao.maikan.R
import com.theoxao.maikan.mvp.MultiResultFragment
import com.theoxao.maikan.MainActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.widget.ImageView
import android.widget.TextView
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.utils.ObjectMapperUtils
import com.theoxao.maikan.utils.ObjectMapperUtils.Companion.objectMapper
import com.theoxao.maikan.utils.PicassoImageLoader
import com.xys.libzxing.zxing.activity.CaptureActivity


class ShelfFragment : MultiResultFragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var presenter: MultiPresenter
    private lateinit var coverView: ImageView
    private lateinit var nameView: TextView
    private lateinit var publisherView: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shelf, container, false)
        super.mContext = context
        super.onCreateView(inflater, container, savedInstanceState)
        fab = view.findViewById(R.id.fab)
        coverView = view.findViewById(R.id.cover)
        nameView = view.findViewById(R.id.book_name)
        publisherView = view.findViewById(R.id.publisher)
        presenter = MultiPresenter(this)
        fab.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context as Activity,
                            arrayOf(Manifest.permission.CAMERA),
                            2);
                } else {
                    showToast("权限已申请");
                }
            }

            val openCameraIntent = Intent(context, CaptureActivity::class.java)
            startActivityForResult(openCameraIntent, 0)
        }
        return view
    }

    override fun onSuccess(target: String, data: String) {
        d(this.javaClass.name, data)
        when (target) {
            "isbn" -> {
                val node = objectMapper.readTree(data)
                val cover = node.get(0).get("cover").asText()
                PicassoImageLoader().displayImage(context, cover, coverView)
                val name = node.get(0).get("name").asText()
                nameView.text = name
                val publisher = node.get(0).get("publisher").asText()
                publisherView.text = publisher
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data == null)
                return
            val isbn = data.extras.getString("result")
            presenter.requestData("http://www.theoxao.com:8888/read/book/isbn/" + isbn, "isbn")
        }
    }
}