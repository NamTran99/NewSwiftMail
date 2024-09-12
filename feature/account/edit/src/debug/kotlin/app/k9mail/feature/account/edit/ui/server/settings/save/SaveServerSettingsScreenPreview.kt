package app.k9mail.feature.account.edit.ui.server.settings.save

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import app.k9mail.feature.account.edit.ui.server.settings.save.fake.FakeSaveServerSettingsViewModel

@Composable
@Preview(showBackground = true)
internal fun SaveServerSettingsScreenK9Preview() {
    PreviewWithThemes {
        SaveServerSettingsScreen(
            title = "Incoming server settings",
            onNext = {},
            onBack = {},
            viewModel = FakeSaveServerSettingsViewModel(
                isIncoming = true,
            ),
        )
    }
}
