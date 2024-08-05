package com.fsck.k9.ui.compose.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.fsck.k9.ui.databinding.ViewUserComposePreviewBinding

class ViewUserComposePreview(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding =
        ViewUserComposePreviewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )

    var name: String = ""
        set(value) {
            field = value
            binding.tvName.text = value
        }
}
