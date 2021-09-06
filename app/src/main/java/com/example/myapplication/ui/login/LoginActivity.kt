package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.base.BaseActivity
import com.example.myapplication.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModel()
    override val layoutId: Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.isLoginSuccess.observe(this, { loginSuccess ->
            if (loginSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        viewModel.errorMessage.observe(this, { message ->
            if (message != "") Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

}