package com.example.lemon.ui.forgetPwd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForgetPwdViewModel : ViewModel() {
    data class ForgetPwdFormState(
        val userInfo: String? = null
    )


    private val _state = MutableLiveData<ForgetPwdState>(ForgetPwdState.PhoneNumber)
    val state: LiveData<ForgetPwdState> = _state

    private val _forgetPwdFormState = MutableLiveData<ForgetPwdFormState>()
    val forgetPwdForm: LiveData<ForgetPwdFormState> = _forgetPwdFormState

    fun changeForgetPwdState() {
        when (state.value) {
            ForgetPwdState.PhoneNumber -> _state.value = ForgetPwdState.MailAddress
            ForgetPwdState.MailAddress -> _state.value = ForgetPwdState.PhoneNumber
            else -> {
                Log.e("lemon", "ForgetPwdViewModel: changeForgetPwdState: state.value cannot be null")
            }
        }
    }

    fun forgetPwdDataChanged(userInfo: String) {
        _forgetPwdFormState.value = ForgetPwdFormState(userInfo = userInfo)
    }
}