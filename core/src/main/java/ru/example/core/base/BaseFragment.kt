package ru.example.core.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewModel, B : ViewBinding>(layoutId: Int) : Fragment(layoutId) {

    protected lateinit var viewModel: V
    protected var binding: B? = null

    protected abstract fun initDi()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDi()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}