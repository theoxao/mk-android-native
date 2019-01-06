package com.theoxao.maikan.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityManagerCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.Book
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.ui.adapter.BookViewAdapter
import com.theoxao.maikan.ui.adapter.BookViewHolder
import com.theoxao.maikan.utils.ItemClickSupport
import com.theoxao.maikan.utils.ObjectMapperUtils

class SelectBookActivity : MultiResultActivity() {

    private lateinit var presenter: MultiPresenter
    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var searchResultView: TextView
    private lateinit var isbn: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_book)
        bookRecyclerView = findViewById(R.id.book_recycler_view)
        searchResultView = findViewById(R.id.search_result_tip)

        presenter = MultiPresenter(this)
        isbn = intent.getStringExtra("isbn")
        presenter.requestData(Routers.ISBN_SEARCH + isbn, "isbn")
    }

    override fun onSuccess(target: String, data: String) {
        when (target) {
            "isbn" -> {
                val books = ObjectMapperUtils.objectMapper.readValue<ArrayList<Book>>(data, ObjectMapperUtils.objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, Book::class.java))
                if (!books.isEmpty()) {
                    searchResultView.text = "通过条码 ${isbn} 找到以下书籍"
                    val adapter = BookViewAdapter(books, this)
                    bookRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    bookRecyclerView.adapter = adapter
                    bookRecyclerView.setHasFixedSize(true)
                    ItemClickSupport.addTo(bookRecyclerView).setOnItemClickListener { rcv, pos, v ->
                        val t = adapter.getItem(pos)
                        val intent = Intent(this@SelectBookActivity, AddBookActivity::class.java)
                        intent.action = Intent.ACTION_VIEW
                        intent.data = Uri.parse(t.cover)
                        intent.putExtra(AddBookActivity.FROM_KEY, AddBookActivity.FROM_SELECT)
                        intent.putExtra("name", t.name)
                        intent.putExtra("author", t.author)
                        intent.putExtra("publisher", t.publisher)
                        intent.putExtra("pageCount", t.pageCount)
                        val holder = rcv.findViewHolderForAdapterPosition(pos) as BookViewHolder
                        val bundle = ActivityOptions.makeSceneTransitionAnimation(
                                this@SelectBookActivity,
                                holder.coverView,
                                holder.coverView.transitionName
                        ).toBundle()
                        startActivity(intent, bundle)
                        this@SelectBookActivity.finish()
                        AddBookActivity.that?.let {
                            it.finish()
                        }
                    }
                }
            }
        }
    }
}
