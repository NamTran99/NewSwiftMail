package com.fsck.k9.job

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.fsck.k9.Account
import com.fsck.k9.K9
import com.fsck.k9.K9.INTERNAL_TIME_MAIL_SYNC_MINUTE
import java.util.concurrent.TimeUnit
import kotlinx.datetime.Clock
import timber.log.Timber

class MailSyncWorkerManager(
    private val workManager: WorkManager,
    val clock: Clock,
) {

    fun cancelMailSync(account: Account) {
        Timber.v("Canceling mail sync worker for %s", account)
        val uniqueWorkName = createUniqueWorkName(account.uuid)
        workManager.cancelUniqueWork(uniqueWorkName)
    }

    fun scheduleMailSync(swiftAccount: Account) {
        if (isNeverSyncInBackground()) return
        Timber.v("Scheduling mail sync worker for %s", swiftAccount)
        Timber.v("  sync interval: %d minutes", INTERNAL_TIME_MAIL_SYNC_MINUTE / 1000 / 60)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val lastSyncTime = swiftAccount.lastSyncTime
        Timber.v("  last sync time: %tc", lastSyncTime)

        val data = workDataOf(MailSyncWorker.EXTRA_ACCOUNT_UUID to swiftAccount.uuid)

        val mailSyncRequest = OneTimeWorkRequestBuilder<MailSyncWorker>()
            .setConstraints(constraints)
            .setInitialDelay(INTERNAL_TIME_MAIL_SYNC_MINUTE, TimeUnit.MILLISECONDS)
            .addTag(MAIL_SYNC_TAG)
            .setInputData(data)
            .build()

        val uniqueWorkName = createUniqueWorkName(swiftAccount.uuid)
        workManager.enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.REPLACE, mailSyncRequest)
    }

    private fun isNeverSyncInBackground() = K9.backgroundOps == K9.BACKGROUND_OPS.NEVER

//    private fun getSyncIntervalIfEnabled(account: Account): Long? {
//        val intervalMinutes = account.automaticCheckIntervalMinutes
//        if (intervalMinutes <= Account.INTERVAL_MINUTES_NEVER) {
//            return null
//        }
//
//        return intervalMinutes.toLong()
//    }

//    private fun calculateInitialDelay(lastSyncTime: Long, syncIntervalMinutes: Long): Long {
//        val now = clock.now().toEpochMilliseconds()
//        val nextSyncTime = lastSyncTime + (syncIntervalMinutes * 60L * 1000L)
//
//        return if (lastSyncTime > now || nextSyncTime <= now) {
//            0L
//        } else {
//            nextSyncTime - now
//        }
//    }

    private fun createUniqueWorkName(accountUuid: String): String {
        return "$MAIL_SYNC_TAG:$accountUuid"
    }

    companion object {
        const val MAIL_SYNC_TAG = "MailSync"
        private const val INITIAL_BACKOFF_DELAY_MINUTES = 1L
    }
}
