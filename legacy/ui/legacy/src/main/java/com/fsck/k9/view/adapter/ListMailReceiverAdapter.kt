package com.fsck.k9.view.adapter

import android.content.Context
import com.fsck.k9.helper.MessageHelper
import com.fsck.k9.mail.Address
import com.fsck.k9.ui.R
import com.fsck.k9.ui.base.extensions.createTransactionID
import com.fsck.k9.ui.base.extensions.show
import com.fsck.k9.ui.base.recyclerview.SingleHolderBindingAdapter
import com.fsck.k9.ui.databinding.ItemMailReceiverBinding
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ListMailReceiverAdapter : SingleHolderBindingAdapter<AddressWithKeyModel, ItemMailReceiverBinding>(),
    KoinComponent {

    override val layoutId: Int
        get() = R.layout.item_mail_receiver

    fun submitAddress(list: MutableList<Address>?) {
        submitList(
            list?.map {
                AddressWithKeyModel(address = it, id = createTransactionID())
            },
        )
    }

    override fun onBind(binding: ItemMailReceiverBinding, context: Context, item: AddressWithKeyModel, position: Int) {
        binding.apply {
            tvTo.show(position == 0)
            viewUser.name = item.address.address
            viewUser.setIconContact(item.address)
        }
    }
}
