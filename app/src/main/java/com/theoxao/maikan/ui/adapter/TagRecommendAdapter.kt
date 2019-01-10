package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.theoxao.maikan.R

class TagRecommendAdapter(private val tags: ArrayList<String>, private val context: Context) : RecyclerView.Adapter<TagRecommendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagRecommendViewHolder {
        return TagRecommendViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_recommend_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: TagRecommendViewHolder, position: Int) {
        holder.tagView.text = tags[position]
    }
}

class TagRecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagView = itemView.findViewById<TextView>(R.id.tag)
}