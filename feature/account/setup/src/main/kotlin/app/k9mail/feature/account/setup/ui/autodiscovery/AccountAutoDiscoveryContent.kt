package app.k9mail.feature.account.setup.ui.autodiscovery

import android.content.res.Resources
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.designsystem.atom.text.TextBodyMedium
import app.k9mail.core.ui.compose.designsystem.molecule.ContentLoadingErrorView
import app.k9mail.core.ui.compose.designsystem.molecule.ErrorView
import app.k9mail.core.ui.compose.designsystem.molecule.LoadingView
import app.k9mail.core.ui.compose.designsystem.molecule.input.EmailAddressInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.PasswordInput
import app.k9mail.core.ui.compose.designsystem.organism.TopAppBarWithBackButton
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveWidthContainer
import app.k9mail.core.ui.compose.theme2.MainTheme
import app.k9mail.feature.account.common.ui.WizardNavigationBar
import app.k9mail.feature.account.common.ui.WizardNavigationBarState
import app.k9mail.feature.account.common.ui.loadingerror.rememberContentLoadingErrorViewState
import app.k9mail.feature.account.oauth.ui.AccountOAuthContract
import app.k9mail.feature.account.oauth.ui.AccountOAuthView
import app.k9mail.feature.account.setup.R
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import app.k9mail.feature.account.setup.ui.autodiscovery.view.AutoDiscoveryResultApprovalView
import app.k9mail.feature.account.setup.ui.autodiscovery.view.AutoDiscoveryResultView
import app.k9mail.feature.account.setup.ui.autodiscovery.view.ListMailLoginView

@Composable
internal fun AccountAutoDiscoveryContent(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    appName: String,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Log.d("TAG", "AccountAutoDiscoveryContent: NamTD8 ${state}")

    ResponsiveWidthContainer(
        modifier = Modifier
            .fillMaxSize()
            .testTag("AccountAutoDiscoveryContent")
            .then(modifier),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopAppBarWithBackButton(
                    title = appName,
                    onBackClick = {
                        onEvent(Event.OnBackClicked)
                    },
                )
                AutoDiscoveryContent(
                    state = state,
                    onEvent = onEvent,
                    oAuthViewModel = oAuthViewModel,
                )

            }
            if (state.isShowToolbar) {
                WizardNavigationBar(
                    onNextClick = { onEvent(Event.OnNextClicked) },
                    onBackClick = { onEvent(Event.OnBackClicked) },
                    state = WizardNavigationBarState(showNext = state.isNextButtonVisible),
                )
            }
        }
    }
}

@Composable
internal fun AutoDiscoveryContent(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    modifier: Modifier = Modifier,
) {
    val resources = LocalContext.current.resources

    ContentLoadingErrorView(
        state = rememberContentLoadingErrorViewState(state),
        loading = {
            LoadingView(
                message = stringResource(id = R.string.account_setup_auto_discovery_loading_message),
                modifier = Modifier.fillMaxSize(),
            )
        },
        error = {
            ErrorView(
                title = stringResource(id = R.string.account_setup_auto_discovery_loading_error),
                message = state.error?.toAutoDiscoveryErrorString(resources),
                onRetry = { onEvent(Event.OnRetryClicked) },
                modifier = Modifier.fillMaxSize(),
            )
        },
        content = {
            ContentView(
                state = state,
                onEvent = onEvent,
                oAuthViewModel = oAuthViewModel,
                resources = resources,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    )
}

@Composable
internal fun ContentView(
    state: State,
    onEvent: (Event) -> Unit,
    oAuthViewModel: AccountOAuthContract.ViewModel,
    resources: Resources,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        if (state.configStep != AccountAutoDiscoveryContract.ConfigStep.LIST_MAIL_SERVER) {
            AutoDiscoveryResultView(
                settings = state.autoDiscoverySettings,
                onEditConfigurationClick = { onEvent(Event.OnEditConfigurationClicked) },
            )
            if (state.autoDiscoverySettings != null && state.autoDiscoverySettings.isTrusted.not()) {
                AutoDiscoveryResultApprovalView(
                    approvalState = state.configurationApproved,
                    onApprovalChange = { onEvent(Event.ResultApprovalChanged(it)) },
                )
            }
            Spacer(modifier = Modifier.height(MainTheme.spacings.double))
        }

        AnimatedVisibility(
            visible = state.configStep in listOf(
                AccountAutoDiscoveryContract.ConfigStep.GMAIL,
                AccountAutoDiscoveryContract.ConfigStep.OUTLOOK,
            ),
        ) {
            Image(
                painter = painterResource(id = state.currentMailState?.drawableResID ?: return@AnimatedVisibility),
                null,
                modifier = Modifier.height(100.dp),
            )
            Spacer(modifier = Modifier.height(MainTheme.spacings.double))
            TextBodyMedium(text = stringResource(id = R.string.account_setup_sign_in_to_your_account))
        }

        if (state.configStep == AccountAutoDiscoveryContract.ConfigStep.LIST_MAIL_SERVER) {
            ListMailLoginView(
                modifier = Modifier.padding(0.dp, MainTheme.spacings.double, 0.dp, 0.dp),
                listMail = state.listMailState,
                onItemClick = {
                    onEvent(Event.OnSelectServer(it))
                },
            )
        }

        if (state.configStep == AccountAutoDiscoveryContract.ConfigStep.PASSWORD) {
            EmailAddressInput(
                emailAddress = state.emailAddress.value,
                errorMessage = state.emailAddress.error?.toAutoDiscoveryValidationErrorString(resources),
                onEmailAddressChange = { onEvent(Event.EmailAddressChanged(it)) },
                contentPadding = PaddingValues(),
            )
            Spacer(modifier = Modifier.height(MainTheme.spacings.double))
            PasswordInput(
                password = state.password.value,
                errorMessage = state.password.error?.toAutoDiscoveryValidationErrorString(resources),
                onPasswordChange = { onEvent(Event.PasswordChanged(it)) },
                contentPadding = PaddingValues(),
            )
        } else if (state.configStep == AccountAutoDiscoveryContract.ConfigStep.OAUTH) {
            val isAutoDiscoverySettingsTrusted = state.autoDiscoverySettings?.isTrusted ?: false
            val isConfigurationApproved = state.configurationApproved.value ?: false
            Spacer(modifier = Modifier.height(MainTheme.spacings.double))
            AccountOAuthView(
                onOAuthResult = { result -> onEvent(Event.OnOAuthResult(result)) },
                viewModel = oAuthViewModel,
                isEnabled = isAutoDiscoverySettingsTrusted || isConfigurationApproved,
            )
        }
    }
}
