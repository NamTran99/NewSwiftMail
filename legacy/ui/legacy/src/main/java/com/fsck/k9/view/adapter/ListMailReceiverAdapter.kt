package com.fsck.k9.view.adapter

import android.content.Context
import com.fsck.k9.mailstore.MessageDetails
import com.fsck.k9.ui.base.recyclerview.SingleHolderBindingAdapter
import com.fsck.k9.ui.databinding.ItemMailReceiverBinding

class ListMailReceiverAdapter: SingleHolderBindingAdapter<MessageDetails, ItemMailReceiverBinding>() {
    override val layoutId: Int
        get() = TODO("Not yet implemented")

    override fun onBind(binding: ItemMailReceiverBinding, context: Context, item: MessageDetails, position: Int) {
        TODO("Not yet implemented")
    }
}
