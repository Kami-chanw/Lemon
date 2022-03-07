package com.example.lemon.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lemon.R

class NewsRecyclerAdapter(val context: Context, private val newsList: List<News>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsTitle: TextView = view.findViewById(R.id.tv_main_news_title)
        //在此处添加每条新闻的缓存内容
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_main_content, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        holder.newsTitle.text = news.title
        //在此处加载News中的内容
    }
    override fun getItemCount() = newsList.size
}