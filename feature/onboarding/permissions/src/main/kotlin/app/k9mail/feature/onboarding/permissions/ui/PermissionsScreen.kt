package app.k9mail.feature.onboarding.permissions.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
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


    val notificationsPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { success ->
        viewModel.event(Event.NotificationsPermissionResult(success))
    }
    val contactsPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { success ->
        if (uiState.value.notificationsPermissionState == PermissionsContract.UiPermissionState.Unknown) {
            notificationsPermissionLauncher.requestNotificationsPermission()
        }
        viewModel.event(Event.ContactsPermissionResult(success))
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

    LaunchedEffect(key1 = Unit) {
        dispatch(Event.LoadPermissionState)
    }

    PermissionsContent(
        state = state.value,
        onEvent = dispatch,
        appName = appNameProvider.appName,
    )
}

private fun ManagedActivityResultLauncher<String, Boolean>.requestContactsPermission() {
    launch(Manifest.permission.READ_CONTACTS)
}

private fun ManagedActivityResultLauncher<String, Boolean>.requestNotificationsPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
