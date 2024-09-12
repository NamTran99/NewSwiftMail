package app.k9mail.feature.account.setup.ui.specialfolders

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes

@Composable
@Preview(showBackground = true)
internal fun SpecialFoldersContentLoadingPreview() {
    PreviewWithThemes {
        SpecialFoldersContent(
            state = SpecialFoldersContract.State(
                isLoading = true,
            ),
            onEvent = {},
            contentPadding = PaddingValues(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun SpecialFoldersContentFormPreview() {
    PreviewWithThemes {
        SpecialFoldersContent(
            state = SpecialFoldersContract.State(
                isLoading = false,
            ),
            onEvent = {},
            contentPadding = PaddingValues(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun SpecialFoldersContentSuccessPreview() {
    PreviewWithThemes {
        SpecialFoldersContent(
            state = SpecialFoldersContract.State(
                isLoading = false,
                isSuccess = true,
            ),
            onEvent = {},
            contentPadding = PaddingValues(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun SpecialFoldersContentErrorPreview() {
    PreviewWithThemes {
        SpecialFoldersContent(
            state = SpecialFoldersContract.State(
                isLoading = false,
                error = SpecialFoldersContract.Failure.LoadFoldersFailed("Error"),
            ),
            onEvent = {},
            contentPadding = PaddingValues(),
            appName = "AppName",
        )
    }
}
