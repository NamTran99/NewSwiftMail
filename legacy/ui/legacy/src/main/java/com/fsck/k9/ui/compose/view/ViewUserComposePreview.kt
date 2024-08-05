package com.fsck.k9.ui.compose.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.k9mail.core.ui.legacy.designsystem.atom.icon.Icons
import com.fsck.k9.activity.misc.ContactPicture
import com.fsck.k9.mail.Address
import com.fsck.k9.ui.databinding.ViewUserComposePreviewBinding

class ViewUserComposePreview(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

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

    fun setIconContact(address: Address?) {
        binding.apply {
            if (address != null) {
                val contactsPictureLoader = ContactPicture.getContactPictureLoader()
                contactsPictureLoader.setContactPicture(imgContactPicture, address)
            } else {
                imgContactPicture.setImageResource(Icons.Outlined.AccountCircle)
            }
        }
    }
}
