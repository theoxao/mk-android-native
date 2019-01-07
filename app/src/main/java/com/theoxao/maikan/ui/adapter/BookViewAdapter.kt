package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ruffian.library.widget.REditText
import com.theoxao.maikan.R
import com.theoxao.maikan.functions.OnClick
import com.theoxao.maikan.model.Book
import com.theoxao.maikan.utils.PicassoImageLoader


class BookViewAdapter(private val books: ArrayList<Book>, private val context: Context) : RecyclerView.Adapter<BookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(LayoutInflater.from(context).inflate(R.layout.book_list_recycler_view, parent, false))
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        PicassoImageLoader().displayCover(context, book.cover, holder.coverView, book.name ?: "")
        holder.nameView.text = book.name
        holder.authorView.text = "${book.author}"
        holder.pageCountView.text = "-${book.pageCount}页"
        holder.publisherView.text = "${book.publisher}"
    }

    fun getItem(position: Int): Book {
        return books[position]
    }
}

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val coverView = itemView.findViewById<ImageView>(R.id.book_cover)!!
    val nameView = itemView.findViewById<TextView>(R.id.name_view)
    val authorView = itemView.findViewById<TextView>(R.id.author_view)
    val pageCountView = itemView.findViewById<TextView>(R.id.page_count_view)
    val publisherView = itemView.findViewById<TextView>(R.id.publisher_view)
}