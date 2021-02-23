package ru.example.core.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PostAdapter<T>(
    vararg delegates: AdapterDelegate<T>
) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected val postModels = mutableListOf<T>()
    private val delegateManager = AdapterDelegateManager<T>()

    val subloadPosition: Int
        get() = postModels.size - 1

    val lastItemPosition: Int
        get() = itemCount - 1

    init {
        delegates.forEach(delegateManager::addDelegate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> =
        delegateManager.getDelegate(
            viewType
        ).onCreateViewHolder(parent)

    override fun getItemCount(): Int = postModels.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(postModels[position])
    }

    override fun getItemViewType(position: Int): Int =
        delegateManager.getItemViewType(postModels[position])

    open fun add(input: T) = with(postModels) {
        add(input)
        notifyItemInserted(size)
    }

    fun add(input: List<T>) = with(postModels) {
        val currentSize = size
        addAll(input)
        notifyItemRangeInserted(currentSize, size)
    }

    fun add(index: Int, input: T) = with(postModels) {
        add(index, input)
        notifyItemInserted(index)
    }

    fun replaceNoAnim(input: List<T>) = with(postModels) {
        clear()
        addAll(input)
        notifyDataSetChanged()
    }

    open fun replaceAll(input: List<T>) = with(postModels) {
        clear()
        addAll(input)
        notifyItemRangeRemoved(0, size)
    }

    open fun replaceWithDiff(input: List<T>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(postModels, input)
        )
        diffResult.dispatchUpdatesTo(this)
        postModels.clear()
        postModels.addAll(input)
    }

    open fun replace(input: List<T>) = with(postModels) {
        postModels.clear()
        postModels.addAll(input)
        notifyItemRangeChanged(0, size)
    }

    fun remove(input: T) = with(postModels) {
        val index = indexOf(input)
        removeAt(index)
        notifyItemRemoved(index)
    }

    fun remove(index: Int) {
        postModels.removeAt(index)
        notifyItemRemoved(index)
    }

    fun change(input: T) = with(postModels) {
        if (contains(input)) {
            val index = indexOf(input)
            removeAt(index)
            add(index, input)
            notifyItemChanged(index)
        }
    }

    fun clear() = with(postModels) {
        val size = size
        clear()
        notifyItemRangeRemoved(0, size)
    }

    fun clearAfter(index: Int) = with(postModels) {
        val size = size
        subList(index, size).clear()
        notifyItemRangeRemoved(index, size)
    }

    fun getPost(position: Int): T {
        return postModels[position]
    }

    fun getDelegate(viewType: Int) = delegateManager.getDelegate(viewType)
}