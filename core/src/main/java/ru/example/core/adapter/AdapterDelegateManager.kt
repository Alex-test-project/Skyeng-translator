package ru.example.core.adapter

import androidx.collection.SparseArrayCompat

class AdapterDelegateManager<T> {
    private var delegates: SparseArrayCompat<AdapterDelegate<T>> = SparseArrayCompat()

    fun addDelegate(delegate: AdapterDelegate<T>) = delegates.put(delegates.size(), delegate)

    fun getDelegate(viewType: Int): AdapterDelegate<T> = delegates[viewType]!!

    fun getItemViewType(viewData: T): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i]!!.isValidType(viewData)) {
                return delegates.keyAt(i)
            }
        }

        throw NullPointerException("Delegate not found for $viewData")
    }
}