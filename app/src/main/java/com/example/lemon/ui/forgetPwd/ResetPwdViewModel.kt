package com.example.lemon.ui.forgetPwd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResetPwdViewModel : ViewModel() {
    data class ResetPwdFormState(
        val newPassword: String? = null,
        val repeatPassword: String? = null
    )


    private val _resetPwdFormState = MutableLiveData<ResetPwdFormState>()
    val resetPwdForm: LiveData<ResetPwdFormState> = _resetPwdFormState

    fun resetPwdDataChanged(newPassword: String, repeatPassword: String) {
        _resetPwdFormState.value =
            ResetPwdFormState(newPassword = newPassword, repeatPassword = repeatPassword)
    }
}