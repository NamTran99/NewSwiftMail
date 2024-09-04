package app.k9mail.feature.onboarding.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.k9mail.core.ui.compose.common.activity.LocalActivity
import app.k9mail.feature.account.setup.navigation.AccountSetupNavHost
import app.k9mail.feature.onboarding.permissions.domain.PermissionsDomainContract.UseCase.HasRuntimePermissions
import app.k9mail.feature.onboarding.permissions.ui.PermissionsScreen
import app.k9mail.feature.onboarding.welcome.ui.WelcomeScreenScreen
import app.k9mail.feature.settings.import.ui.SettingsImportScreen
import org.koin.compose.koinInject

private const val NESTED_NAVIGATION_ROUTE_WELCOME = "list_host_mail"
private const val NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP = "account_setup"
private const val NESTED_NAVIGATION_ROUTE_SETTINGS_IMPORT = "settings_import"
private const val NESTED_NAVIGATION_ROUTE_PERMISSIONS = "permissions"

private fun NavController.navigateToAccountSetup() {
    navigate(NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP)
}

private fun NavController.navigateToSettingsImport() {
    navigate(NESTED_NAVIGATION_ROUTE_SETTINGS_IMPORT)
}

private fun NavController.navigateToPermissions() {
    navigate(NESTED_NAVIGATION_ROUTE_PERMISSIONS) {
        popUpTo(NESTED_NAVIGATION_ROUTE_WELCOME) {
            inclusive = true
        }
    }
}

@Composable
fun OnboardingNavHost(
    onFinish: (String?) -> Unit,
    hasRuntimePermissions: HasRuntimePermissions = koinInject(),
) {
    val navController = rememberNavController()
    var accountUuid by rememberSaveable { mutableStateOf<String?>(null) }
    val activity = LocalActivity.current

    NavHost(
        navController = navController,
        startDestination = NESTED_NAVIGATION_ROUTE_PERMISSIONS,
    ) {

        composable(route = NESTED_NAVIGATION_ROUTE_ACCOUNT_SETUP) {
            AccountSetupNavHost(
                onBack = { if(!navController.popBackStack()) activity.finish() },
                onFinish = { createdAccountUuid: String ->
                    accountUuid = createdAccountUuid
                    if (hasRuntimePermissions()) {
                        navController.navigateToPermissions()
                    } else {
                        onFinish(createdAccountUuid)
                    }
                },
            )
        }

        composable(route = NESTED_NAVIGATION_ROUTE_WELCOME) {
            WelcomeScreenScreen(
                onStartClick = { navController.navigateToAccountSetup() },
                onImportClick = { navController.navigateToSettingsImport() },
                appNameProvider = koinInject(),
            )
        }

        composable(route = NESTED_NAVIGATION_ROUTE_SETTINGS_IMPORT) {
            SettingsImportScreen(
                onImportSuccess = {
                    if (hasRuntimePermissions()) {
                        navController.navigateToPermissions()
                    } else {
                        onFinish(null)
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(route = NESTED_NAVIGATION_ROUTE_PERMISSIONS) {
            PermissionsScreen(
                onNext = { onFinish(accountUuid) },
            )
        }
    }
}
