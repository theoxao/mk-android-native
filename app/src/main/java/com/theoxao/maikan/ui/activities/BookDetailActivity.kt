package com.theoxao.maikan.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.constant.Routers
import com.theoxao.maikan.model.Excerpt
import com.theoxao.maikan.model.ReadLog
import com.theoxao.maikan.mvp.MultiPresenter
import com.theoxao.maikan.mvp.MultiResultActivity
import com.theoxao.maikan.ui.adapter.ExcerptAdapter
import com.theoxao.maikan.ui.adapter.ReadLogAdapter
import com.theoxao.maikan.utils.DateUtils
import com.theoxao.maikan.utils.ObjectMapperUtils.Companion.objectMapper
import com.theoxao.maikan.utils.PicassoImageLoader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookDetailActivity : MultiResultActivity() {

    companion object {
        const val USER_BOOK_INTENT_KEY = "user_book_id"
    }

    private lateinit var presenter: MultiPresenter
    private lateinit var userBookId: String
    private lateinit var coverUrl: String
    private lateinit var coverView: ImageView
    private lateinit var refBookJson: String
    private lateinit var nameView: TextView
    private lateinit var authorView: TextView
    private lateinit var addTimeView: TextView
    private lateinit var introEntry: TextView
    private lateinit var readLogRecyclerView: RecyclerView
    private lateinit var excerptListRecyclerView: RecyclerView
    //stat
    private lateinit var progressTitle: TextView
    private lateinit var progressView: TextView
    private lateinit var remainDuration: TextView
    private lateinit var returnView: TextView
    private lateinit var returnInfo: ConstraintLayout

    private lateinit var requestBody: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        presenter = MultiPresenter(this)
        val data = intent.data
        coverUrl = data.toString()
        userBookId = intent.getStringExtra(USER_BOOK_INTENT_KEY)
        val name = intent.getStringExtra("name")

        coverView = findViewById(R.id.book_cover)
        nameView = findViewById(R.id.book_name)
        authorView = findViewById(R.id.book_author)
        addTimeView = findViewById(R.id.add_time)
        introEntry = findViewById(R.id.introduction_entry)
        progressTitle = findViewById(R.id.progress_title)
        progressView = findViewById(R.id.progress)
        remainDuration = findViewById(R.id.remain_duration)
        returnView = findViewById(R.id.return_view)
        readLogRecyclerView = findViewById(R.id.read_log_recycler_view)
        excerptListRecyclerView = findViewById(R.id.excerpt_recycler_view)

        PicassoImageLoader().displayCover(this, coverUrl, coverView, name)
        nameView.text = name
        authorView.text = intent.getStringExtra("author")
        requestBody = HashMap()
        requestBody["id"] = userBookId
        presenter.getData(Routers.BOOK_DETAIL, requestBody, "book_detail")
        presenter.getData(Routers.READ_STAT, requestBody, "read_stat")
        requestBody["size"] = "0"
        presenter.getData(Routers.READ_LOG, requestBody, "read_log")
        requestBody = HashMap()
        requestBody["size"] = "1"
        requestBody["page"] = "1"
        requestBody["bookId"] = userBookId
        presenter.getData(Routers.EXCERPT_LIST, requestBody, "excerpt_list")
        d(this.localClassName, userBookId)
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(target: String, data: String) {
        when (target) {
            "book_detail"  -> {
                val node = objectMapper.readTree(data)
                val date = DateUtils.parseDate(Date(node.get("createAt").asLong()))
                introEntry.visibility = if (node.get("refBook").asText() == "null") View.GONE else View.VISIBLE
                val publisher = node.get("publisher")?.asText() ?: ""
                authorView.text = "${authorView.text}·$publisher"
                addTimeView.text = "添加于$date"
            }
            "read_log"     -> {
                val records = objectMapper.readValue<ArrayList<ReadLog>>(data, objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, ReadLog::class.java))
                readLogRecyclerView.layoutManager = GridLayoutManager(this@BookDetailActivity, 1)
                readLogRecyclerView.adapter = ReadLogAdapter(records, this@BookDetailActivity)
            }
            "excerpt_list" -> {
                val list = objectMapper.readValue<ArrayList<Excerpt>>(data, objectMapper.typeFactory.constructCollectionType(ArrayList::class.java, Excerpt::class.java))
                excerptListRecyclerView.layoutManager = GridLayoutManager(this@BookDetailActivity, 1)
                excerptListRecyclerView.adapter = ExcerptAdapter(list, this@BookDetailActivity)
            }
            "read_stat"    -> {
                val node = objectMapper.readTree(data)
                if (node.get("finished") != null)
                    progressTitle.text = progressTitle.text.toString() + "(已完成)"
                progressView.text = node.get("progress").asText()
                remainDuration.text = DateUtils.parseDuration(node.get("totalDuration").asLong())
                if (node.get("remainDays") == null)
                    returnInfo.visibility = View.GONE
                else {
                    returnView.text = "需在${node.get("remainDays").asText()}天后归还"
                }
            }
        }
    }
}
