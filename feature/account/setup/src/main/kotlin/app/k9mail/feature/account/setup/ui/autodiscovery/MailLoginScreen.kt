package app.k9mail.feature.account.setup.ui.autodiscovery

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.k9mail.core.common.provider.AppNameProvider
import app.k9mail.core.ui.compose.common.mvi.observe
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.AutoDiscoveryUiResult
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Effect
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ViewModel
import kotlinx.coroutines.delay

@Composable
internal fun MailLoginScreen(
    onNext: (AutoDiscoveryUiResult) -> Unit,
    onBack: () -> Unit,
    viewModel: ViewModel,
    appNameProvider: AppNameProvider,
    modifier: Modifier = Modifier,
) {
    val (state, dispatch) = viewModel.observe { effect ->

        when (effect) {
            Effect.NavigateBack -> onBack()
            is Effect.NavigateNext -> onNext(effect.result)
        }
    }

    BackHandler {
        dispatch(Event.OnBackClicked)
    }

    LaunchedEffect(Unit) {
        delay(300)
        dispatch(Event.OnScreenShown)
    }

    AccountAutoDiscoveryContent(
        state = state.value,
        onEvent = { dispatch(it) },
        oAuthViewModel = viewModel.oAuthViewModel,
        appName = appNameProvider.appName,
        modifier = modifier,
    )
}
