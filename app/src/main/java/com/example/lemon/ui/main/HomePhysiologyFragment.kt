package com.example.lemon.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lemon.R


class HomePhysiologyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.frag_main_home_physiology, container, false)
        if (activity != null) {
            val newsList = listOf(News("新闻-生理-1"), News("新闻-生理-2"), News("新闻-生理-3"))

            val recyclerView: RecyclerView = view.findViewById(R.id.rv_main_home_physiology)
            val layoutManager = LinearLayoutManager(context)
            val adapter = context?.let { NewsRecyclerAdapter(it, newsList) }
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
        return view
    }

}