package app.k9mail.feature.onboarding.permissions.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import app.k9mail.core.common.provider.AppNameProvider
import app.k9mail.core.ui.compose.common.mvi.observe
import app.k9mail.feature.onboarding.permissions.ui.PermissionsContract.Effect
import app.k9mail.feature.onboarding.permissions.ui.PermissionsContract.Event
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

fun openAppSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(
            Settings.ACTION_APP_NOTIFICATION_SETTINGS,
        )
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        context.startActivity(intent)
    }
}

@Composable
fun PermissionsScreen(
    viewModel: PermissionsContract.ViewModel = koinViewModel<PermissionsViewModel>(),
    appNameProvider: AppNameProvider = koinInject(),
    onNext: () -> Unit,
) {

    val context = LocalContext.current

    val uiState = viewModel.state
    val contactsPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { success ->
        viewModel.event(Event.ContactsPermissionResult(success))
    }
    val notificationsPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { success ->
        if(uiState.value.contactsPermissionState == PermissionsContract.UiPermissionState.Unknown){
            contactsPermissionLauncher.requestContactsPermission()
        }
        viewModel.event(Event.NotificationsPermissionResult(success))
    }



    val (state, dispatch) = viewModel.observe { effect ->
        when (effect) {
            Effect.RequestContactsPermission -> contactsPermissionLauncher.requestContactsPermission()
            Effect.RequestNotificationsPermission -> {
                notificationsPermissionLauncher.requestNotificationsPermission()
            }

            Effect.NavigateToNotificationSetting -> {
                openAppSettings(context)
            }

            Effect.NavigateNext -> onNext()
        }
    }

    BackHandler {
        // no back navigation
    }

    OnLifecycleEvent{
            owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> { dispatch(Event.LoadPermissionState) }
            else                      -> { /* other stuff */ }
        }
    }

    PermissionsContent(
        state = state.value,
        onEvent = dispatch,
        appName = appNameProvider.appName,
    )
}
@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

private fun ManagedActivityResultLauncher<String, Boolean>.requestContactsPermission() {
    launch(Manifest.permission.READ_CONTACTS)
}

private fun ManagedActivityResultLauncher<String, Boolean>.requestNotificationsPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
