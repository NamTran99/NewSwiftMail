package app.k9mail.feature.account.oauth.ui

import androidx.compose.runtime.Composable
import app.k9mail.core.ui.compose.common.annotation.PreviewDevices
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes

@Composable
@PreviewDevices
internal fun AccountOAuthContentPreview() {
    PreviewWithThemes {
        AccountOAuthContent(
            state = AccountOAuthContract.State(),
            onEvent = {},
        )
    }
}
