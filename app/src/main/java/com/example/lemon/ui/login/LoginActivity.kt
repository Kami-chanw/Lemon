package com.example.lemon.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import com.example.lemon.R
import com.example.lemon.databinding.ActivityLoginBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Bmob.initialize(this, "d9b92ebb1e6479816175ef48881e29ae")

        binding.imgBtnLoginClose.setOnClickListener { finish() }

        viewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
        viewModel.loginResult.observe(this) {
            it?.run {
                error?.run {
                    showLoginFailed(error)
                    viewModel.loginResult.value = LoginViewModel.LoginResult()
                }
                success?.run {
                    updateUiWithUser(success)
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        if (model.displayName != "") {
            val welcome = getString(R.string.welcome)
            val displayName = model.displayName
            // TODO : initiate successful logged in experience
            Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        BmobUser.logOut()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}