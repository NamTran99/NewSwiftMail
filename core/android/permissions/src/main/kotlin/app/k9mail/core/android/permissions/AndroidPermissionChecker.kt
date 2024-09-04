package app.k9mail.core.android.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import org.koin.core.component.getScopeName

/**
 * Checks if a [Permission] has been granted to the app.
 */
class AndroidPermissionChecker(
    private val context: Context,
    private val dataStore: AppPermissionDataStore,
) : PermissionChecker {

    override suspend fun checkPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.Contacts -> {
                checkSelfPermission(permission.permissionText)
            }

            Permission.Notifications -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkSelfPermission(permission.permissionText)
                } else {
                    PermissionState.GrantedImplicitly
                }
            }
        }
    }

    private suspend fun checkSelfPermission(permission: String): PermissionState {
        return if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            PermissionState.Granted
        } else {
            if (dataStore.isPermissionBlock(permission)) PermissionState.DeniedForever else
                PermissionState.DeniedTemporary
        }
    }

    override suspend fun increaseDenyAndCheckIfBlock(permission: Permission): Boolean {
        dataStore.increaseDeclineTime(permission.permissionText)
        return dataStore.isPermissionBlock(permission.permissionText)
    }
}
