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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cn.bmob.v3.BmobUser
import com.example.lemon.R
import com.example.lemon.databinding.FragLoginSmsBinding
import com.example.lemon.utils.StyledClickableSpan
import com.google.android.material.bottomsheet.BottomSheetDialog


class LoginSmsFragment : Fragment(R.layout.frag_login_sms) {
    companion object {
        @JvmStatic
        fun newInstance() = LoginSmsFragment()
    }

    private lateinit var binding: FragLoginSmsBinding
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
        binding = FragLoginSmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = binding.etLoginPhoneNumber
        val sendCode = binding.btnLoginSendCode
        val agreeContact = binding.ckbLoginAgree


        agreeContact.isChecked = viewModel.loginFormState.value?.hasAgreed ?: false

        viewModel.loginFormState.value?.username?.let {
            if (it.isPhoneNumber())
                phoneNumber.setText(it)
        }

        viewModel.loginFormState.observe(viewLifecycleOwner) {
            it?.apply {
                sendCode.isEnabled =
                    phoneNumber.text.toString().isPhoneNumber() and hasAgreed
            }
        }

        phoneNumber.afterTextChanged { callOnDataChanged() }

        sendCode.setOnClickListener {
            veriCodeLauncher.launch(Intent().apply {
                component = ComponentName(
                    "com.example.lemon",
                    "com.example.lemon.ui.veriCode.VeriCodeActivity"
                )
                putExtra("phoneNumber", phoneNumber.text.toString())
                putExtra("intent", "sign or login")
            })
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

        binding.btnLoginSmsHelp.setOnClickListener {
            BottomSheetDialog(requireContext()).run {
                setContentView(R.layout.item_login_sms_help)
                show()
            }

        }

        binding.btnLoginPwdLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_login, LoginPasswordFragment.newInstance())
                .commit()
        }

    }

    private fun callOnDataChanged() {
        viewModel.loginDataChanged(
            binding.etLoginPhoneNumber.text.toString(),
            binding.ckbLoginAgree.isChecked
        )
    }
}