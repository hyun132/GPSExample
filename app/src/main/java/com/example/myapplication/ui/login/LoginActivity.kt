package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@LoginActivity
            viewModel = loginViewModel
        }
        setContentView(binding.root)

        loginViewModel.checkSession()
        loginViewModel.isLoginSuccess.observe(this, { loginSuccess ->
            if (loginSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        loginViewModel.errorMessage.observe(this, {
            if (it != "") Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

}