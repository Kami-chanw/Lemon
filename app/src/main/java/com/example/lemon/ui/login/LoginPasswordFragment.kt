package com.example.lemon.ui.login

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cn.bmob.v3.BmobUser
import com.example.lemon.R
import com.example.lemon.databinding.FragLoginPasswordBinding
import com.example.lemon.utils.StyledClickableSpan
import com.google.android.material.bottomsheet.BottomSheetDialog


class LoginPasswordFragment : Fragment(R.layout.frag_login_password) {
    companion object {
        @JvmStatic
        fun newInstance() = LoginPasswordFragment()
    }

    private lateinit var binding: FragLoginPasswordBinding
    private val viewModel by activityViewModels<LoginViewModel>()
    private val veriCodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK)
            viewModel.loginResult.value =
                LoginViewModel.LoginResult(success = LoggedInUserView(BmobUser.getCurrentUser().username))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragLoginPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = binding.etLoginUsername
        val password = binding.etLoginPwd
        val loginBtn = binding.btnLoginLogin
        val agreeContact = binding.ckbLoginAgree

        viewModel.loginFormState.value?.let {
            agreeContact.isChecked = it.hasAgreed
            username.setText(it.username)
            password.setText(it.password)
        }

        viewModel.loginFormState.observe(viewLifecycleOwner) { state ->
            state?.let {
                loginBtn.isEnabled =
                    viewModel.isUserNameValid(it.username) and viewModel.isPasswordValid(it.password) and it.hasAgreed
            }
        }

        username.afterTextChanged { callOnDataChanged() }
        password.afterTextChanged { callOnDataChanged() }

        loginBtn.setOnClickListener {
            viewModel.loginByAccount(username.text.toString(), password.text.toString())
        }

        agreeContact.run {
            text = SpannableString("我已阅读并同意《青檬论坛条款》").apply {
                setSpan(
                    StyledClickableSpan(context) {
                        startActivity(
                            Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("http://www.baidu.com")
                            )
                        )
                    },
                    indexOf("《"),
                    indexOf("》") + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            setOnCheckedChangeListener { _, _ ->
                callOnDataChanged()
            }

            movementMethod = LinkMovementMethod.getInstance()
        }

        binding.btnLoginPwdHelp.setOnClickListener {
            BottomSheetDialog(requireContext()).run {
                setContentView(R.layout.item_login_pwd_help)
                findViewById<Button>(R.id.btn_login_forget_pwd)?.setOnClickListener {
                    veriCodeLauncher.launch(Intent().apply {
                        component = ComponentName(
                            "com.example.lemon",
                            "com.example.lemon.ui.forgetPwd.ForgetPwdActivity"
                        )
                    })
                }
                show()
            }

        }

        binding.btnLoginSmsLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_login, LoginSmsFragment.newInstance())
                .commit()
        }

    }

    private fun callOnDataChanged() {
        viewModel.loginDataChanged(
            binding.etLoginUsername.text.toString(),
            binding.etLoginPwd.text.toString(),
            binding.ckbLoginAgree.isChecked
        )
    }
}