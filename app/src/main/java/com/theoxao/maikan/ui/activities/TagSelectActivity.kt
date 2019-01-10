package com.theoxao.maikan.ui.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.Tag
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.ui.adapter.TagSelectAdapter
import com.theoxao.maikan.ui.adapter.TagSelectViewHolder
import com.theoxao.maikan.utils.ItemClickSupport
import com.theoxao.maikan.utils.ObjectMapperUtils

class TagSelectActivity : MultiResultActivity() {

    private lateinit var presenter: MultiPresenter
    private lateinit var tagSelectRecyclerView: RecyclerView
    private lateinit var selected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_select)
        tagSelectRecyclerView = findViewById(R.id.tag_select_recycler_view)
        selected = intent.getStringExtra("selectTag")
        presenter = MultiPresenter(this)
        presenter.requestData(Routers.TAG_LIST, null)

    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            Routers.TAG_LIST -> {
                val tags = ObjectMapperUtils.objectMapper.readValue<ArrayList<Tag>>(data, ObjectMapperUtils.objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, Tag::class.java))
                tagSelectRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                tagSelectRecyclerView.adapter = TagSelectAdapter(tags, this, selected)
                ItemClickSupport.addTo(tagSelectRecyclerView).setOnItemClickListener { recyclerView, position, v ->
                    val holder = recyclerView.findViewHolderForAdapterPosition(position) as TagSelectViewHolder
                    holder.checkBox.isChecked = true
                    val resultIntent = Intent()
                    val bundle = Bundle()
                    bundle.putString("tag", holder.tagView.text.toString())
                    resultIntent.putExtras(bundle)
                    this@TagSelectActivity.setResult(Activity.RESULT_OK, resultIntent)
                    this@TagSelectActivity.finish()
                }
            }
        }
    }
}
