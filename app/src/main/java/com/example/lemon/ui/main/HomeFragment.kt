package com.example.lemon.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.example.lemon.R


class HomeFragment : Fragment() {
    //首页的Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.frag_main_home, container, false)
        if (activity != null) {
            val fragmentList = listOf<Fragment>(HomePhysiologyFragment())

            val viewPager: ViewPager = view.findViewById(R.id.vp_main_home_physiology)
            val adapter = ViewPagerAdapter(parentFragmentManager, fragmentList)
            viewPager.adapter = adapter
        }
        return view
    }
}