package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gcssloop.widget.RCRelativeLayout
import com.ruffian.library.widget.RRelativeLayout
import com.theoxao.maikan.R
import com.theoxao.maikan.utils.ColorUtils

class TagMangerAdapter(private val tags: ArrayList<Tag>, private val context: Context) :
    RecyclerView.Adapter<TagManagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagManagerViewHolder {
        return TagManagerViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.tag_manager_recycler_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: TagManagerViewHolder, position: Int) {
        val tag = tags[position]
        holder.tagView.text = tag.name
        if (!tag.editable) {
            holder.container.helper.backgroundColorNormal = ColorUtils.colorLight
            holder.tagView.setTextColor(ColorUtils.colorPrimary)
            holder.removeView.visibility = View.INVISIBLE
        }
    }
}

class TagManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagView = itemView.findViewById<TextView>(R.id.tag)
    val removeView = itemView.findViewById<ImageView>(R.id.remove)
    val container = itemView.findViewById<RRelativeLayout>(R.id.container)
}

class Tag {
    var name: String? = null
    var editable = true

    constructor()
    constructor(name: String?, editable: Boolean) {
        this.name = name
        this.editable = editable
    }


}