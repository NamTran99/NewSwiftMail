package app.k9mail.feature.account.server.settings.ui.incoming

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import app.k9mail.core.ui.compose.common.annotation.PreviewDevices
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import app.k9mail.feature.account.common.domain.entity.InteractionMode

@Composable
@PreviewDevices
internal fun IncomingServerSettingsContentPreview() {
    PreviewWithThemes {
        IncomingServerSettingsContent(
            mode = InteractionMode.Create,
            onEvent = { },
            state = IncomingServerSettingsContract.State(),
            contentPadding = PaddingValues(),
        )
    }
}
