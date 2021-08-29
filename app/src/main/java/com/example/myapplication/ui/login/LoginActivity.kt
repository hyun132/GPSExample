package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvLoginButton.setOnClickListener {
                val username = etId.text.toString()
                val password = etPw.text.toString()
                Log.d("MainActivity", "로그인 버튼 클릭")
                if (username.isEmpty() || password.isEmpty()) println("아이디오 비밀번호 입력해주세요")
                else loginViewModel.logIn(LoginRequest(username = username, password = password))
            }
        }

        loginViewModel.checkSession()
        loginViewModel.isLoginSuccess.observe(this, { loginSuccess ->
            if (loginSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}