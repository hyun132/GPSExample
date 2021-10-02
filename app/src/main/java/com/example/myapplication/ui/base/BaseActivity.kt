package com.example.myapplication.ui.base

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myapplication.BR
import com.example.myapplication.R

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> :
    AppCompatActivity() {

    lateinit var binding: T
    abstract val viewModel: V
    abstract val layoutId: Int
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.apply {
            lifecycleOwner = this@BaseActivity
            setVariable(BR.viewModel, viewModel)
        }
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        viewModel.isLoading.observe(this, {
            when (it) {
                true -> {
                    dialog.show()
                }
                false -> {
                    dialog.hide()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
}