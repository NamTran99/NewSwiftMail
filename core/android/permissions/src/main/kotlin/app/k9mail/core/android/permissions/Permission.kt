package app.k9mail.core.android.permissions

import android.Manifest
import android.os.Build

/**
 * System permissions we ask for during onboarding.
 */
enum class Permission(val permissionText: String) {
    Contacts(Manifest.permission.READ_CONTACTS),
    Notifications(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else ""),
}
