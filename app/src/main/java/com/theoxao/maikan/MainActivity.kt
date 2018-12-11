package com.theoxao.maikan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.theoxao.maikan.ui.activities.IndexActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, IndexActivity::class.java))
        finish()
    }
}
