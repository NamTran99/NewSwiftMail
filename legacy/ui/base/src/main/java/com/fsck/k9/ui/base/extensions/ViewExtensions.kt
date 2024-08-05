package com.fsck.k9.ui.base.extensions

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.util.UUID


fun <T : ViewDataBinding> ViewGroup.inflateBinding(
    @LayoutRes layoutId: Int,
    attach: Boolean = false,
): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, attach)
}

fun <T : View> T.show(b: Boolean = true, function: T.() -> Unit = {}) {
    visibility = if (b) {
        function(this)
        View.VISIBLE
    } else View.GONE
}

fun <T : View> T.invisible(b: Boolean = true, function: T.() -> Unit = {}) {
    visibility = if (b) {
        function(this)
        View.INVISIBLE
    } else View.VISIBLE
}

fun Context.loadAttrs(attrs: AttributeSet?, attrType: IntArray, function: TypedArray.() -> Unit) {
    if (attrs == null) return
    val a = obtainStyledAttributes(attrs, attrType)
    function(a)
    a.recycle()
}

fun View.hide(b: Boolean = true) {
    visibility = if (b) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun EditText.disableAndReceiveClick() {
    isEnabled = false
    movementMethod = null
    keyListener = null
}

fun ImageView.toggleImage(
    isActive: Boolean,
    activeImage: Int,
    inActiveImage: Int,
    onClickListener: ((Boolean) -> Unit)? = null,
) {
    var mIsActive = isActive
    setImageResource(if (mIsActive) activeImage else inActiveImage)
    setOnClickListener {
        mIsActive = !isActive
        setImageResource(if (isActive) activeImage else inActiveImage)
        onClickListener?.invoke(mIsActive)
    }
}

fun Context.getThemeColor(@AttrRes color: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(color, typedValue, true)
    return typedValue.data
}

fun String.removeAllDuplicateWhiteSpace(): String {
    return replace("\\s+".toRegex(), " ")
}

fun EditText.showKeyboard() {
    postDelayed(
        {
            requestFocus()
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            setSelection(text.length)
        },
        200,
    )
}

fun EditText.addTextChangedListenerNotFirst(callback: (String) -> Unit) {
    var isFirstTime = true

    addTextChangedListener {
        if(isFirstTime){
            isFirstTime = false
        }else{
            callback.invoke(it.toString())
        }
    }
}
