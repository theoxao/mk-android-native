package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.model.Excerpt

class ExcerptAdapter(private val list: ArrayList<Excerpt>, private val context: Context) :
    RecyclerView.Adapter<ExcerptViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcerptViewHolder {
        return ExcerptViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.excerpt_list_recycler_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExcerptViewHolder, position: Int) {
        val excerpt = list[position]
        holder.contentVIew.text = excerpt.content
        holder.imageRecyclerView.layoutManager = GridLayoutManager(context, 3)
        holder.imageRecyclerView.adapter = ImageListAdapter(excerpt.images, context)
    }
}


class ExcerptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val contentVIew: TextView = itemView.findViewById(R.id.content)
    val imageRecyclerView: RecyclerView = itemView.findViewById(R.id.image_list_recycler_view)
}