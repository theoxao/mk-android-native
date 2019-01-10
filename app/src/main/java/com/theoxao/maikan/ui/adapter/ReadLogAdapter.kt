package com.theoxao.maikan.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.theoxao.maikan.R
import com.theoxao.maikan.model.ReadLog
import com.theoxao.maikan.utils.DateUtils
import java.util.*

class ReadLogAdapter(private val logs: ArrayList<ReadLog>, private val context: Context) : RecyclerView.Adapter<ReadLogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadLogViewHolder {
        return ReadLogViewHolder((LayoutInflater.from(context).inflate(R.layout.read_log_recycler_view, parent, false)))
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReadLogViewHolder, position: Int) {
        val log = logs[position]
        holder.pageCountView.text = "${log.currentPage}页"
        holder.startAtView.text = log.startAt?.let { "•${DateUtils.parseDate(it)}" }
        holder.durationView.text = DateUtils.parseDuration(log.duration)
    }
}


class ReadLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pageCountView: TextView = itemView.findViewById(R.id.page_count)
    val startAtView: TextView = itemView.findViewById(R.id.start_at)
    val durationView: TextView = itemView.findViewById(R.id.duration)
}