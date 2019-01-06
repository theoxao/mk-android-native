package com.theoxao.maikan.ui.activities

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.view.View
import com.theoxao.maikan.R
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.ui.fragments.CircleFragment
import com.theoxao.maikan.ui.fragments.HomeFragment
import com.theoxao.maikan.ui.fragments.ShelfFragment
import com.theoxao.maikan.utils.BottomBar
import com.theoxao.maikan.utils.SharedPreferencesUtil

class IndexActivity : MultiResultActivity() {

    lateinit var bottomBar: BottomBar
    private lateinit var fragmentContainer: View
    lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        fm = supportFragmentManager
        bottomBar = findViewById(R.id.bottom_bar)
        fragmentContainer = findViewById(R.id.navigate_fragment_container)

        bottomBar.setContainer(R.id.navigate_fragment_container)
                .setTitleBeforeAndAfterColor("#999999", "#ff5d5e")
                .addItem(ShelfFragment::class.java, "书架", R.drawable.ic_tabs_allbooks_normal, R.drawable.ic_tabs_allbooks_selected)
                .addItem(CircleFragment::class.java, "小组", R.drawable.ic_tabs_group_normal, R.drawable.ic_tabs_group_selected)
                .addItem(HomeFragment::class.java, "我的", R.drawable.ic_tabs_usercenter_normal, R.drawable.ic_tabs_usercenter_selected)
                .setFirstChecked(SharedPreferencesUtil.getInstance().getInt("last_visit_bar", 0))
                .build()
    }

    override fun onSuccess(target: String, data: String) {

    }
}
