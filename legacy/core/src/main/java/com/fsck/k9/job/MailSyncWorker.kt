package com.fsck.k9.job

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
import android.content.ContentResolver
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fsck.k9.Account
import com.fsck.k9.K9
import com.fsck.k9.Preferences
import com.fsck.k9.controller.MessagingController
import com.fsck.k9.mail.AuthType
import timber.log.Timber

class MailSyncWorker(
    private val messagingController: MessagingController,
    private val preferences: Preferences,
    private val mailSyncWorkerManager: MailSyncWorkerManager,
    context: Context,
    parameters: WorkerParameters,
) : Worker(context, parameters) {

    override fun doWork(): Result {
        val accountUuid = inputData.getString(EXTRA_ACCOUNT_UUID)
        requireNotNull(accountUuid)

        Timber.d("Executing periodic mail sync for account %s", accountUuid)

        if (isBackgroundSyncDisabled()) {
            Timber.d("Background sync is disabled. Skipping mail sync.")
            return Result.success()
        }

        val account = preferences.getAccount(accountUuid)
        if (account == null) {
            Timber.e("Account %s not found. Can't perform mail sync.", accountUuid)
            return Result.failure()
        }

        if (account.incomingServerSettings.isMissingCredentials) {
            Timber.d("Password for this account is missing. Skipping mail sync.")
            return Result.success()
        }

        if (account.incomingServerSettings.authenticationType == AuthType.XOAUTH2 && account.oAuthState == null) {
            Timber.d("Account requires sign-in. Skipping mail sync.")
            return Result.success()
        }

        val success = messagingController.performPeriodicMailSync(account, isForeGrounded())
        Timber.d("Account sync state ${success}")
        mailSyncWorkerManager.scheduleMailSync(account)
        return if (success) Result.success() else Result.retry()
    }

    private fun isForeGrounded(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE)
    }

    private fun isBackgroundSyncDisabled(): Boolean {
        return when (K9.backgroundOps) {
            K9.BACKGROUND_OPS.NEVER -> true
            K9.BACKGROUND_OPS.ALWAYS -> false
            K9.BACKGROUND_OPS.WHEN_CHECKED_AUTO_SYNC -> !ContentResolver.getMasterSyncAutomatically()
        }
    }

    companion object {
        const val EXTRA_ACCOUNT_UUID = "accountUuid"
    }
}
