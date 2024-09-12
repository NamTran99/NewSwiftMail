package app.k9mail.core.ui.compose.designsystem.template

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.k9mail.core.ui.compose.common.annotation.PreviewDevices
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import app.k9mail.core.ui.compose.designsystem.atom.Surface
import app.k9mail.core.ui.compose.theme2.MainTheme

@Composable
@PreviewDevices
internal fun ResponsiveContentWithBackgroundPreview() {
    PreviewWithThemes {
        ResponsiveContentWithSurface {
            Surface(
                color = MainTheme.colors.info,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MainTheme.spacings.double),
                content = {},
            )
        }
    }
}
