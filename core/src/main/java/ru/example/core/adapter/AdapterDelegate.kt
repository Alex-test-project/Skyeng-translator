package ru.example.core.adapter

import android.view.ViewGroup

interface AdapterDelegate<T> {

    fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<T>

    fun isValidType(viewData: T): Boolean
}