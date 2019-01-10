package com.theoxao.maikan.ui.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.Tag
import com.theoxao.maikan.model.enums.TagFixed
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultFragment
import com.theoxao.maikan.ui.activities.AddBookActivity
import com.theoxao.maikan.ui.activities.TagMangerActivity
import com.theoxao.maikan.utils.ItemClickSupport
import com.theoxao.maikan.utils.ObjectMapperUtils.Companion.objectMapper


class ShelfFragment : MultiResultFragment() {

    lateinit var fab: FloatingActionButton
    private lateinit var presenter: MultiPresenter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var tagManagerEntry: ImageView
    var indicators = arrayListOf<String>()
    var pageFragments = arrayListOf<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shelf, container, false)
        super.mContext = context
        super.onCreateView(inflater, container, savedInstanceState)
        super.mContext = context
        fab = view.findViewById(R.id.fab)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        tagManagerEntry = view.findViewById(R.id.tag_manager_entry)

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setTabTextColors(
            ContextCompat.getColor(context!!, R.color.fourthText),
            ContextCompat.getColor(context!!, R.color.colorPrimary)
        )
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        tabLayout.setupWithViewPager(viewPager)

        viewPager = view.findViewById(R.id.view_pager)
        presenter = MultiPresenter(this)
        fab.setOnClickListener {
            val intent = Intent(context, AddBookActivity::class.java)
            intent.putExtra(AddBookActivity.FROM_KEY, AddBookActivity.FROM_SHELF)
            this@ShelfFragment.startActivityForResult(intent, 0)
        }
        presenter.requestData(Routers.TAG_LIST, null)

        tagManagerEntry.setOnClickListener {
            startActivity(Intent(context, TagMangerActivity::class.java))
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            childFragmentManager.fragments.forEach { it ->
                if (it is TagPageFragment && it.isVisible) {
                    it.reload()
                }
            }
        }
    }

    override fun onSuccess(target: String, data: String) {
        d(this.javaClass.name, data)
        when (target) {
            Routers.TAG_LIST -> {
                val tags = objectMapper.readValue<ArrayList<Tag>>(
                    data,
                    objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, Tag::class.java)
                )
                TagFixed.values().forEach {
                    indicators.add(it.display)
                    val bundle = Bundle()
                    val f = TagPageFragment()
                    bundle.putString("tag", it.code)
                    f.arguments = bundle
                    pageFragments.add(f)
                }


                tags.forEach { tag ->
                    tag.tag?.let {
                        indicators.add(it)
                        val bundle = Bundle()
                        val f = TagPageFragment()
                        bundle.putString("tag", it)
                        f.arguments = bundle
                        pageFragments.add(f)
                    }
                }
                viewPager.adapter = ViewPageAdapter(childFragmentManager)
            }
        }
    }

    inner class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return pageFragments[position]
        }

        override fun getCount(): Int {
            return indicators.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return indicators[position]
        }
    }
}
