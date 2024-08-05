package com.fsck.k9.ui.base.recyclerview

import android.annotation.SuppressLint

open class KeyCallbackItem<MODEL : KeyModel> : BaseCallbackItem<MODEL>() {
    override fun areItemsTheSame(oldItem: MODEL, newItem: MODEL): Boolean {
        return oldItem.identity == newItem.identity
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: MODEL, newItem: MODEL): Boolean {
           return oldItem == newItem
    }
}
