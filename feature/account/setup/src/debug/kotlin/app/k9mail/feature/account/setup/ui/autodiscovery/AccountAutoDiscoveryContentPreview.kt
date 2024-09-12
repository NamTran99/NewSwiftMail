package app.k9mail.feature.account.setup.ui.autodiscovery

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.k9mail.core.ui.compose.designsystem.PreviewWithThemes
import app.k9mail.feature.account.common.domain.input.StringInputField
import app.k9mail.feature.account.server.validation.ui.fake.FakeAccountOAuthViewModel
import app.k9mail.feature.account.setup.ui.autodiscovery.fake.fakeAutoDiscoveryResultSettings

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentEmailPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(
                emailAddress = StringInputField(value = "test@example.com"),
            ),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentPasswordPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(
                configStep = AccountAutoDiscoveryContract.ConfigStep.PASSWORD,
                emailAddress = StringInputField(value = "test@example.com"),
                autoDiscoverySettings = fakeAutoDiscoveryResultSettings(isTrusted = true),
            ),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentPasswordUntrustedSettingsPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(
                configStep = AccountAutoDiscoveryContract.ConfigStep.PASSWORD,
                emailAddress = StringInputField(value = "test@example.com"),
                autoDiscoverySettings = fakeAutoDiscoveryResultSettings(isTrusted = false),
            ),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentPasswordNoSettingsPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(
                configStep = AccountAutoDiscoveryContract.ConfigStep.PASSWORD,
                emailAddress = StringInputField(value = "test@example.com"),
            ),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun AccountAutoDiscoveryContentOAuthPreview() {
    PreviewWithThemes {
        AccountAutoDiscoveryContent(
            state = AccountAutoDiscoveryContract.State(
                configStep = AccountAutoDiscoveryContract.ConfigStep.OAUTH,
                emailAddress = StringInputField(value = "test@example.com"),
                autoDiscoverySettings = fakeAutoDiscoveryResultSettings(isTrusted = true),
            ),
            onEvent = {},
            oAuthViewModel = FakeAccountOAuthViewModel(),
            appName = "AppName",
        )
    }
}


//@Composable
//@Preview(showBackground = true)
//internal fun AccountAutoDiscoveryContentGmailPreview() {
//    PreviewWithThemes {
//        AccountAutoDiscoveryContent(
//            state = AccountAutoDiscoveryContract.State(
//                configStep = AccountAutoDiscoveryContract.ConfigStep.GMAIL,
//                currentMailState = AccountAutoDiscoveryContract.MailState.GMAIL,
//                emailAddress = StringInputField(value = "test@example.com"),
//                autoDiscoverySettings = fakeAutoDiscoveryResultSettings(isTrusted = true),
//            ),
//            onEvent = {},
//            oAuthViewModel = FakeAccountOAuthViewModel(),
//            appName = "AppName",
//        )
//    }
//}
