package ru.example.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T> : RecyclerView.ViewHolder {

    constructor(
        parent: ViewGroup,
        layoutId: Int
    ) : super(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    constructor(view: View) : super(view)

    abstract fun bind(data: T)
}