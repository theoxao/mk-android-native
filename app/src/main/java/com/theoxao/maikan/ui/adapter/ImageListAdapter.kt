package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.theoxao.maikan.R
import com.theoxao.maikan.utils.PicassoImageLoader

class ImageListAdapter(private val list: MutableList<String>, private val context: Context) :
    RecyclerView.Adapter<ImageListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.image_list_recycler_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        PicassoImageLoader().displayImage(context, list[position], holder.imageView)
    }
}

class ImageListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.image_view)
}