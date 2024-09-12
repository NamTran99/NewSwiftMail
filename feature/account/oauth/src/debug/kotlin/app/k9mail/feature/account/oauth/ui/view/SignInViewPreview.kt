package app.k9mail.feature.account.oauth.ui.view

import androidx.compose.runtime.Composable
import app.k9mail.core.ui.compose.common.annotation.PreviewDevices
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes

@PreviewDevices
@Composable
internal fun SignInViewPreview() {
    PreviewWithThemes {
        SignInView(
            onSignInClick = {},
            isGoogleSignIn = false,
        )
    }
}

@PreviewDevices
@Composable
internal fun SignInViewWithGooglePreview() {
    PreviewWithThemes {
        SignInView(
            onSignInClick = {},
            isGoogleSignIn = true,
        )
    }
}
