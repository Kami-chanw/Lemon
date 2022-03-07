package com.example.lemon.ui.forgetPwd

sealed class ForgetPwdState {
    object PhoneNumber : ForgetPwdState()
    object MailAddress : ForgetPwdState()
}