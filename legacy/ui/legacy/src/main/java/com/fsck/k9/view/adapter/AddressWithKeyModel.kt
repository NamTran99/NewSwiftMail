package com.fsck.k9.view.adapter

import com.fsck.k9.mail.Address
import com.fsck.k9.ui.base.recyclerview.KeyModel

class AddressWithKeyModel(val address: Address, val id: String): KeyModel {
    override val identity = id
}
