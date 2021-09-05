package com.example.myapplication.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.myapplication.BR
import android.R
import android.util.Log
import java.lang.Exception


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {
//    val TAG = this::class.java.simpleName
    lateinit var binding: T
    abstract val viewModel: V
    abstract var layoutResourceId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        try {
            binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
            // ... rest of body of onCreateView() ...
        } catch (e: Exception) {
            Log.e("BASE", "onCreateView", e)
            throw e
        }
//        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.setVariable(BR.viewModel,viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.executePendingBindings()
//    }

    override fun onDestroy() {
        super.onDestroy()

        binding.lifecycleOwner=null
    }
    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }
}