package app.k9mail.feature.onboarding.permissions.domain.usecase

import app.k9mail.core.android.permissions.Permission
import app.k9mail.core.android.permissions.PermissionChecker
import app.k9mail.feature.onboarding.permissions.domain.PermissionsDomainContract

/**
 * Checks if a [Permission] has been granted to the app.
 */
class SaveBlockPermission(
    private val permissionChecker: PermissionChecker,
) : PermissionsDomainContract.UseCase.IncreaseDenyAndCheckIfBlock {

    override suspend fun invoke(permission: Permission): Boolean {
        return permissionChecker.increaseDenyAndCheckIfBlock(permission)
    }
}
