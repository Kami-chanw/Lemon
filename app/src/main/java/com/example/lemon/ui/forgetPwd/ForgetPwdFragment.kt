package com.example.lemon.ui.forgetPwd

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.example.lemon.R
import com.example.lemon.databinding.FragForgetPwdBinding
import com.example.lemon.ui.login.afterTextChanged
import com.example.lemon.ui.login.isMailAddress
import com.example.lemon.ui.login.isPhoneNumber

class ForgetPwdFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = ForgetPwdFragment()
    }


    private lateinit var viewModel: ForgetPwdViewModel
    private lateinit var binding: FragForgetPwdBinding
    private val veriCodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.layout_forget_pwd, ResetPwdFragment.newInstance())
                    .commit()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragForgetPwdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgetPwdViewModel::class.java)

        val userInfo = binding.etForgetPwdUserinfo
        val confirm = binding.btnForgetPwdConfirm
        val changeState = binding.btnForgetPwdChange

        changeState.setOnClickListener {
            viewModel.changeForgetPwdState()
            userInfo.setText("")
            viewModel.forgetPwdDataChanged("")
        }

        userInfo.afterTextChanged {
            viewModel.forgetPwdDataChanged(userInfo.text.toString())
        }

        viewModel = ViewModelProvider(this).get(ForgetPwdViewModel::class.java)
        viewModel.forgetPwdForm.observe(viewLifecycleOwner) {
            it?.let {
                when (viewModel.state.value) {
                    ForgetPwdState.PhoneNumber -> confirm.isEnabled =
                        userInfo.text.toString().isPhoneNumber()
                    ForgetPwdState.MailAddress -> confirm.isEnabled =
                        userInfo.text.toString().isMailAddress()
                    else -> {
                        Log.e(
                            "lemon",
                            "ForgetPwdFragment : onViewCreated: viewModel.state cannot be null",
                        )
                    }
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                ForgetPwdState.PhoneNumber -> {
                    userInfo.hint = "手机号"
                    changeState.text = "通过绑定邮箱找回密码"
                }
                ForgetPwdState.MailAddress -> {
                    userInfo.hint = "邮箱"
                    changeState.text = "通过手机号找回密码"
                }
                else -> {
                    Log.e(
                        "lemon",
                        "ForgetPwdFragment : onViewCreated: ForgetPwdState cannot be null",
                    )
                }
            }
        }

        confirm.setOnClickListener {
            val userData = userInfo.text.toString()
            when (viewModel.state.value) {
                ForgetPwdState.PhoneNumber -> {
                    veriCodeLauncher.launch(Intent().apply {
                        component = ComponentName(
                            "com.example.lemon",
                            "com.example.lemon.ui.veriCode.VeriCodeActivity"
                        )
                        putExtra("intent", "reset password")
                        putExtra("phoneNumber", userData)
                    })
                }
                ForgetPwdState.MailAddress -> {
                    BmobUser.resetPasswordByEmail(userData, object : UpdateListener() {
                        override fun done(e: BmobException?) {
                            Toast.makeText(
                                context,
                                if (e != null) "请求验证邮件失败:" + e.message else "请求验证邮件成功，请到" + userInfo + "邮箱中进行激活。",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                else -> {
                    Log.e(
                        "lemon",
                        "ForgetPwdFragment : onViewCreated: viewModel.state cannot be null",
                    )
                }
            }
        }
    }

}