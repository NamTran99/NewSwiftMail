package app.k9mail.ui.catalog.ui.atom

import androidx.compose.runtime.Composable
import app.k9mail.core.ui.compose.common.annotation.PreviewDevicesWithBackground
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import kotlinx.collections.immutable.persistentListOf

@Composable
@PreviewDevicesWithBackground
internal fun CatalogContentPreview() {
    PreviewWithThemes {
        CatalogAtomContent(
            pages = persistentListOf(CatalogAtomPage.TYPOGRAPHY, CatalogAtomPage.COLOR),
            initialPage = CatalogAtomPage.TYPOGRAPHY,
        )
    }
}
