package ru.example.core.di.viewmodelprovider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory<V : ViewModel>(private val viewModel: V) :
	ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (!modelClass.isAssignableFrom(viewModel.javaClass)) {
			throw IllegalArgumentException("Unknown viewmodel class name")
		}
		return viewModel as T
	}
}