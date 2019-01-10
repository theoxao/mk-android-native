package com.theoxao.maikan.ui.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.Tag
import com.theoxao.maikan.model.enums.TagFixed
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.ui.adapter.TagMangerAdapter
import com.theoxao.maikan.ui.adapter.TagRecommendAdapter
import com.theoxao.maikan.utils.ObjectMapperUtils
import com.theoxao.maikan.utils.ObjectMapperUtils.Companion.objectMapper

class TagMangerActivity : MultiResultActivity() {

    private lateinit var presenter: MultiPresenter
    private lateinit var tagListRecyclerView: RecyclerView
    private lateinit var tagRecommendRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_manger)
        presenter = MultiPresenter(this)
        tagListRecyclerView = findViewById(R.id.tag_list_recycler_view)
        tagRecommendRecyclerView = findViewById(R.id.tag_recommend_recycler_view)
        presenter.requestData(Routers.TAG_LIST, null)
        presenter.requestData(Routers.TAG_RECOMMEND, null)
        presenter = MultiPresenter(this)
    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            Routers.TAG_LIST      -> {
                val tags = objectMapper.readValue<ArrayList<Tag>>(data, ObjectMapperUtils.objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, Tag::class.java))
                val tagList: ArrayList<com.theoxao.maikan.ui.adapter.Tag> = arrayListOf()
                TagFixed.values().forEach {
                    tagList.add(com.theoxao.maikan.ui.adapter.Tag(it.display, false))
                }
                tags.forEach {
                    tagList.add(com.theoxao.maikan.ui.adapter.Tag(it.tag, true))
                }
                tagListRecyclerView.adapter = TagMangerAdapter(tagList, this@TagMangerActivity)
                tagListRecyclerView.layoutManager = ChipsLayoutManager.newBuilder(this@TagMangerActivity)
                    .setChildGravity(Gravity.CENTER)
                    .setScrollingEnabled(false)
                    .build()
            }
            Routers.TAG_RECOMMEND -> {
                val tags = objectMapper.readValue<ArrayList<String>>(data, objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, String::class.java))
                tagRecommendRecyclerView.layoutManager = ChipsLayoutManager.newBuilder(this@TagMangerActivity)
                    .setScrollingEnabled(false)
                    .setChildGravity(Gravity.CENTER)
                    .build()
                tagRecommendRecyclerView.adapter = TagRecommendAdapter(tags, this@TagMangerActivity)
            }
        }
    }
}
