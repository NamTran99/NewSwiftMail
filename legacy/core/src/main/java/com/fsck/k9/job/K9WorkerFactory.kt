package com.fsck.k9.job

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

class K9WorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
         val exampleWorkerClassName = Pair(
            "com.hungbang.email2018.service.NewMailCheckWorker",
            "com.fsck.k9.job.MailSyncWorker"
        )
        Log.d("TAG", "createWorker: NamTD8 ${workerClassName}")
            val className = when (workerClassName) {
                exampleWorkerClassName.first -> exampleWorkerClassName.second
                else -> workerClassName
            }

        val workerClass = Class.forName(className).kotlin
        return getKoin().getOrNull(workerClass) { parametersOf(workerParameters) }
    }
}
