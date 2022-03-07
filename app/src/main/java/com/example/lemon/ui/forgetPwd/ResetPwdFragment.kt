package com.example.lemon.ui.forgetPwd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.example.lemon.databinding.FragForgetPwdResetBinding
import com.example.lemon.ui.login.afterTextChanged

class ResetPwdFragment : Fragment() {
    private lateinit var binding: FragForgetPwdResetBinding
    private lateinit var viewModel: ResetPwdViewModel

    companion object {
        @JvmStatic
        fun newInstance() =
            ResetPwdFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragForgetPwdResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirm = binding.btnForgetPwdConfirm
        val newPassword = binding.etForgetPwdPassword
        val repeatPassword = binding.etForgetPwdRepeat

        viewModel = ViewModelProvider(this).get(ResetPwdViewModel::class.java)

        viewModel.resetPwdForm.observe(viewLifecycleOwner) {
            it?.let {
                confirm.isEnabled =
                    newPassword.text.toString().length >= 8 && newPassword.text.toString() == repeatPassword.text.toString()
            }
        }

        newPassword.afterTextChanged { onDataChanged() }
        repeatPassword.afterTextChanged { onDataChanged() }

        confirm.setOnClickListener {
            BmobUser.getCurrentUser().run {
                setPassword(newPassword.text.toString())
                update(object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        Toast.makeText(
                            requireContext(),
                            if (e != null) e.message else "密码重置成功",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
            requireActivity().run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("newPassword", newPassword.text.toString())
                })
                finish()
            }
        }
    }

    private fun onDataChanged() {
        viewModel.resetPwdDataChanged(
            binding.etForgetPwdPassword.text.toString(),
            binding.etForgetPwdRepeat.text.toString()
        )
    }

}