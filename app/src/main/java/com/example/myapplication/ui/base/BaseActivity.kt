package com.example.myapplication.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myapplication.BR

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),BaseFragment.Callback {

    lateinit var binding: T
    abstract val viewModel: V
    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        binding.apply {
            lifecycleOwner = this@BaseActivity
            setVariable(BR.viewModel,viewModel)
        }
    }
}