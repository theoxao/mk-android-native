package com.theoxao.maikan.ui.fragments


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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

class TagPageFragment : MultiResultFragment() {

    private lateinit var presenter: MultiPresenter
    private lateinit var tagName: String
    private var requestBody = HashMap<String, Any>()
    private lateinit var noResultView: ImageView
    private lateinit var shelfBookRecyclerView: RecyclerView
    private var parentFragment: ShelfFragment? = null
    private val fade = AlphaAnimation(1f, 0f)
    private val show = AlphaAnimation(0f, 1f)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tag_page, container, false)
        fade.duration = 400
        show.duration = 400

        noResultView = view.findViewById(R.id.no_result_view)
        shelfBookRecyclerView = view.findViewById(R.id.shelf_book_recycler_view)
        tagName = arguments?.getString("tag") ?: ""
        activity?.supportFragmentManager?.fragments?.forEach {
            if (it is ShelfFragment)
                parentFragment = it
        }
        parentFragment?.let {
            println("yes it has parent fragment")
            shelfBookRecyclerView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        it.fab.alpha = 0f
                        it.fab.startAnimation(fade)
                    }
                    MotionEvent.ACTION_UP -> {
                        it.fab.startAnimation(show)
                        it.fab.alpha = 1f
                    }
                }
                false
            }

        }
        if (tagName == TagFixed.ALL.code)
            tagName = ""
        presenter = MultiPresenter(this)
        requestBody["tag"] = tagName
        presenter.getData(Routers.SHELF_TAG_LIST, requestBody, "shelf_tag_list")
        return view
    }

    fun reload() {
        presenter.getData(Routers.SHELF_TAG_LIST, requestBody, "shelf_tag_list")
    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            "shelf_tag_list" -> {
                val shelfBooks = objectMapper.readValue<ArrayList<ShelfBook>>(data, objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, ShelfBook::class.java))
                if (shelfBooks.isEmpty())
                    noResultView.visibility = View.VISIBLE
                else
                    noResultView.visibility = View.GONE
                if (shelfBookRecyclerView.itemDecorationCount > 0)
                    shelfBookRecyclerView.removeItemDecorationAt(0)
                shelfBookRecyclerView.layoutManager = GridLayoutManager(context, 3)
                shelfBookRecyclerView.addItemDecoration(GridSpacingItemDecoration(3, context!!.resources.getDimensionPixelOffset(R.dimen.dp_16), false))
                shelfBookRecyclerView.adapter = ShelfBookListAdapter(shelfBooks, context!!)
            }
        }
    }

}
