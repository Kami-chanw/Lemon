package com.example.lemon.ui.login

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.example.lemon.data.LemonUser


class LoginViewModel : ViewModel() {
    data class LoginResult(
        val success: LoggedInUserView? = null,
        val error: String? = null
    )

    data class LoginFormState(
        val username: String? = null,
        val password: String? = null,
        val hasAgreed: Boolean = false,
    )

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    val loginResult = MutableLiveData<LoginResult>()

    fun loginByAccount(username: String, password: String) {
        BmobUser.loginByAccount(username, password, object : LogInListener<LemonUser>() {
            override fun done(user: LemonUser?, ex: BmobException?) {
                ex?.run {
                    loginResult.value = LoginResult(error = message)
                } ?: run {
                    user?.run {
                        loginResult.value = LoginResult(success = LoggedInUserView(user.tableName))
                    }
                }
            }
        })
    }

    fun loginDataChanged(username: String, password: String, checkState: Boolean) {
        _loginForm.value = LoginFormState(
            username = username,
            password = password,
            hasAgreed = checkState
        )
    }

    fun loginDataChanged(phoneNumber: String, checkState: Boolean) {
        _loginForm.value =
            LoginFormState(
                username = phoneNumber,
                hasAgreed = checkState
            )
    }

    // A placeholder username validation check
    fun isUserNameValid(username: String?): Boolean =
        username?.run {
            if (contains('@'))
                isMailAddress()
            else
                isPhoneNumber()
        } ?: false


    // A placeholder password validation check
    fun isPasswordValid(password: String?): Boolean = password?.length ?: 0 >= 8
}

fun String.isPhoneNumber(): Boolean =
    this.isDigitsOnly() && this.matches(Regex("^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}\$"))

fun String.isMailAddress(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()