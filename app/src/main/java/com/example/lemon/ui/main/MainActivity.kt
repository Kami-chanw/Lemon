package com.example.lemon.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lemon.R
import com.example.lemon.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.tbMainTitle)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.fabMainNew.setOnClickListener {
            BottomSheetDialog(this).also {
                it.setContentView(
                    layoutInflater.inflate(
                        R.layout.item_home_fab,
                        null
                    )
                )
            }.show()
        }

        switchFragment(HomeFragment())

        binding.bnvMainBottomBar.setOnItemSelectedListener setOnNavigationItemSelectedListener@{
            when (it.itemId) {
                R.id.item_home -> {
                    switchFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                /*R.id.item_course -> {
                    switchFragment(CourseFragment())
                    return@setOnNavigationItemSelectedListener true
                }*/
                else -> return@setOnNavigationItemSelectedListener true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_search, menu)
        return true
    }

    private fun switchFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.home_container, fragment)
        transaction.commit()
    }
}