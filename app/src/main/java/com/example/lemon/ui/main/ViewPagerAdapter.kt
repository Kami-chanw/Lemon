package com.example.lemon.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, private val fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fragmentManager) {

    // 2
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // 3
    override fun getCount(): Int {
        return fragmentList.size
    }
}