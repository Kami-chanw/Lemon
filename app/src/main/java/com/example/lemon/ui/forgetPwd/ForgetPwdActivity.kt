package com.example.lemon.ui.forgetPwd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lemon.R
import com.example.lemon.databinding.ActivityForgetPwdBinding

class ForgetPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPwdBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBtnForgetPwdBack.setOnClickListener { finish() }

        if (intent.getStringExtra("intent") == "init password") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_forget_pwd, ResetPwdFragment.newInstance())
                .commit()
        }


    }
}