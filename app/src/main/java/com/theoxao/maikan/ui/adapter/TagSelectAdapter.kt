package com.theoxao.maikan.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.model.Tag

class TagSelectAdapter(private val tags: ArrayList<Tag>, private val context: Context, private val selected: String) : RecyclerView.Adapter<TagSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagSelectViewHolder {
        return TagSelectViewHolder(LayoutInflater.from(context).inflate(R.layout.tag_select_recycler_view, parent, false))
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: TagSelectViewHolder, position: Int) {
        val tag = tags[position]
        holder.tagView.text = tag.tag
        if (selected == tag.tag)
            holder.checkBox.isChecked = true
    }
}


class TagSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagView = itemView.findViewById<TextView>(R.id.tag)
    val checkBox = itemView.findViewById<RadioButton>(R.id.radioButton)
}