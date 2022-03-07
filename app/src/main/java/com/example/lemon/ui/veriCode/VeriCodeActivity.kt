package com.example.lemon.ui.veriCode

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.example.lemon.data.LemonUser
import com.example.lemon.databinding.ActivityVeriCodeBinding
import com.example.lemon.widget.VeriCodeView
import kotlin.concurrent.thread

class VeriCodeActivity : AppCompatActivity() {
    companion object {
        const val MAX_WAIT_SECONDS = 59
        var seconds = MAX_WAIT_SECONDS
    }

    private lateinit var binding: ActivityVeriCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeriCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = intent.getStringExtra("phoneNumber") as String
        val voiceCode = binding.btnVeriCodeVoice
        val veriCode = binding.vcvVeriCodeInput
        val resend = binding.btnVeriCodeResend

        binding.imgBtnVeriCodeBack.setOnClickListener { finish() }
        binding.tvVeriCodeTo.text = String.format("验证码已发送至 %s", phoneNumber)

        resend.setOnClickListener {
            requestSMSCode(phoneNumber)
        }

        veriCode.setTextWatcher(object : VeriCodeView.VeriCodeTextWatcher {
            override fun onTextChanged(text: String?) {
                if (text?.length == 6) {
                    when (intent.getStringExtra("intent")) {
                        "verify code" -> verifySMSCode(phoneNumber, text)
                        "sign or login" -> signOrLogin(phoneNumber, text)
                        "reset password" -> resetPassword(phoneNumber, text)
                        else -> {
                            Log.e(
                                "lemon",
                                "onTextChanged: no intent for verification code activity"
                            )
                        }
                    }

                }
            }
        })

        if (resend.isEnabled)
            resend.callOnClick()

    }

    private fun startSendingTimer() {
        //val voiceCode = binding.btnVeriCodeVoice
        val resend = binding.btnVeriCodeResend
        resend.isEnabled = false
        //voiceCode.isVisible = false
        thread {
            while (seconds > 0) {
                runOnUiThread {
                    resend.text = String.format("%ds 后可重新获取", seconds)
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                seconds--
            }
            seconds = MAX_WAIT_SECONDS
            runOnUiThread {
                resend.text = "重新发送验证码"
                resend.isEnabled = true
                //voiceCode.isVisible = true
            }
        }
    }

    private fun verifySMSCode(phoneNumber: String, code: String) {
        BmobSMS.verifySmsCode(phoneNumber, code, object : UpdateListener() {
            override fun done(e: BmobException?) {
                e?.run {
                    Toast.makeText(this@VeriCodeActivity, e.message, Toast.LENGTH_SHORT).show()
                } ?: run {
                    setResult(RESULT_OK)
                    finish()
                }

            }
        })
    }

    private fun resetPassword(phoneNumber: String, code: String) {
        BmobUser.loginBySMSCode(phoneNumber, code, object : LogInListener<LemonUser>() {
            override fun done(user: LemonUser?, e: BmobException?) {
                e?.run {
                    Toast.makeText(this@VeriCodeActivity, e.message, Toast.LENGTH_SHORT).show()
                } ?: run {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        })

    }

    private fun signOrLogin(phoneNumber: String, code: String) {
        BmobUser.signOrLoginByMobilePhone(phoneNumber, code, object : LogInListener<LemonUser>() {
            override fun done(user: LemonUser?, e: BmobException?) {
                e?.run {
                    Toast.makeText(this@VeriCodeActivity, e.message, Toast.LENGTH_SHORT).show()
                } ?: run {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        })
    }

    private fun requestSMSCode(phoneNumber: String) {
        BmobSMS.requestSMSCode(phoneNumber, "青檬登录", object : QueryListener<Int>() {
            override fun done(p0: Int?, e: BmobException?) {
                e?.run {
                    Toast.makeText(
                        this@VeriCodeActivity,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } ?: run {
                    Toast.makeText(this@VeriCodeActivity, "验证码发送成功", Toast.LENGTH_SHORT).show()
                    startSendingTimer()
                }
            }
        })
    }
}