package com.theoxao.maikan.ui.fragments


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.enums.ShelfBook
import com.theoxao.maikan.model.enums.TagFixed
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultFragment
import com.theoxao.maikan.ui.adapter.ShelfBookListAdapter
import com.theoxao.maikan.ui.views.GridSpacingItemDecoration
import com.theoxao.maikan.utils.ObjectMapperUtils.Companion.objectMapper
import com.theoxao.maikan.utils.ScreenUtils

class TagPageFragment : MultiResultFragment() {

    private lateinit var presenter: MultiPresenter
    private lateinit var tagName: String
    private var requestBody = HashMap<String, Any>()
    private lateinit var noResultView: ImageView
    private lateinit var shelfBookRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tag_page, container, false)
        noResultView = view.findViewById(R.id.no_result_view)
        shelfBookRecyclerView = view.findViewById(R.id.shelf_book_recycler_view)
        tagName = arguments.getString("tag")
        if (tagName == TagFixed.ALL.code)
            tagName = ""
        presenter = MultiPresenter(this)
        requestBody["tag"] = tagName
        presenter.getData(Routers.SHELF_TAG_LIST, requestBody, "shelf_tag_list")
        return view
    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            "shelf_tag_list" -> {
                val shelfBooks = objectMapper.readValue<ArrayList<ShelfBook>>(data, objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, ShelfBook::class.java))
                if (shelfBooks.isEmpty())
                    noResultView.visibility = View.VISIBLE
                else
                    noResultView.visibility = View.GONE

                shelfBookRecyclerView.layoutManager = object : GridLayoutManager(context, 3) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                shelfBookRecyclerView.addItemDecoration(GridSpacingItemDecoration(3, ScreenUtils.dpToPxInt(18f), false))
                shelfBookRecyclerView.adapter = ShelfBookListAdapter(shelfBooks, context)
            }
        }
    }

}
