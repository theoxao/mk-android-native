package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.theoxao.maikan.R

class ImageListAdapter(private val list: MutableList<String>, private val context: Context) :
    RecyclerView.Adapter<ImageListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.image_list_recycler_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
    }
}

class ImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}