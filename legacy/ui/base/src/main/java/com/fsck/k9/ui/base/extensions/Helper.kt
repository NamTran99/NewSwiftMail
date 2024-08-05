package com.fsck.k9.ui.base.extensions

import java.util.UUID

typealias CallBackNoParam = (() -> Unit)
typealias CallBackParam<T> = ((T) -> Unit)

fun createTransactionID(): String {
    return UUID.randomUUID().toString()
}
