package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.model.enums.ShelfBook
import com.theoxao.maikan.utils.PicassoImageLoader

class ShelfBookListAdapter(private val shelfBooks: ArrayList<ShelfBook>, private val context: Context) : RecyclerView.Adapter<ShelfBookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfBookViewHolder {
        return ShelfBookViewHolder(LayoutInflater.from(context).inflate(R.layout.shelf_book_recycler_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return shelfBooks.size
    }

    override fun onBindViewHolder(holder: ShelfBookViewHolder, position: Int) {
        val record = shelfBooks[position]
        holder.bookName.text = record.name
        holder.bookAuthor.text = record.author
        PicassoImageLoader().displayCover(context, record.cover, holder.bookCover, record.name ?: "")
        if (position == 0) {
            holder.recentBook.visibility = View.VISIBLE
            holder.recentBook.bringToFront()
        }
    }
}


class ShelfBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val bookCover = itemView.findViewById<ImageView>(R.id.book_cover)
    val bookName = itemView.findViewById<TextView>(R.id.book_name)
    val bookAuthor = itemView.findViewById<TextView>(R.id.book_author)
    val recentBook = itemView.findViewById<TextView>(R.id.recentRead)
}