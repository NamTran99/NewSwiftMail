package app.k9mail.feature.onboarding.permissions.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import app.k9mail.core.ui.compose.designsystem.atom.icon.IconsWithBottomRightOverlay

@Composable
@Preview(showBackground = true)
internal fun PermissionBoxUnknownStatePreview() {
    PreviewWithThemes {
        PermissionBox(
            icon = IconsWithBottomRightOverlay.person,
            permissionState = PermissionsContract.UiPermissionState.Unknown,
            title = "Contacts",
            description = "Allow access to be able to display contact names and photos.",
            onAllowClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun PermissionBoxGrantedStatePreview() {
    PreviewWithThemes {
        PermissionBox(
            icon = IconsWithBottomRightOverlay.person,
            permissionState = PermissionsContract.UiPermissionState.Granted,
            title = "Contacts",
            description = "Allow access to be able to display contact names and photos.",
            onAllowClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun PermissionBoxDeniedStatePreview() {
    PreviewWithThemes {
        PermissionBox(
            icon = IconsWithBottomRightOverlay.person,
            permissionState = PermissionsContract.UiPermissionState.Denied,
            title = "Contacts",
            description = "Allow access to be able to display contact names and photos.",
            onAllowClick = {},
        )
    }
}
