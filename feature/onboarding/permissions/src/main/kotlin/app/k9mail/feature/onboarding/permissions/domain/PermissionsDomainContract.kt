package app.k9mail.feature.onboarding.permissions.domain

import app.k9mail.core.android.permissions.Permission
import app.k9mail.core.android.permissions.PermissionState

interface PermissionsDomainContract {

    interface UseCase {

        fun interface CheckPermission {
           suspend  operator fun invoke(permission: Permission): PermissionState
        }

        fun interface HasRuntimePermissions {
            operator fun invoke(): Boolean
        }
        fun interface IncreaseDenyAndCheckIfBlock{
            suspend operator fun invoke(permission: Permission):  Boolean
        }
    }
}
