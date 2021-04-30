package com.arcthos.arcthosmart.shared.syncError.manager

import com.salesforce.androidsdk.mobilesync.target.SyncDownTarget
import com.salesforce.androidsdk.mobilesync.target.SyncUpTarget
import com.salesforce.androidsdk.mobilesync.util.SyncState
import org.json.JSONException
import timber.log.Timber
import com.arcthos.arcthosmart.model.syncerror.SyncDownErrorLog
import com.arcthos.arcthosmart.model.syncerror.SyncUpErrorLog
import com.arcthos.arcthosmart.network.syncError.SyncDownErrorLogRepository
import com.arcthos.arcthosmart.network.syncError.SyncUpErrorLogRepository
import java.io.IOException
import java.util.*

class SyncErrorLogManager(private val syncUpErrorLogRepository: SyncUpErrorLogRepository,
                          private val syncDownErrorLogRepository: SyncDownErrorLogRepository,
                          private val syncState: SyncState,
                          private val syncType: SyncType) {
    fun logSync() {
        if (syncType == SyncType.UP) {
            logSyncUp()
        } else if (syncType == SyncType.DOWN) {
            logSyncDown()
        }
    }

    private fun logSyncUp() {
        val syncUpTarget = syncState.target as SyncUpTarget
        val syncUpErrors = syncUpTarget.syncUpErrors
        val syncUpErrorLogs: MutableList<SyncUpErrorLog> = ArrayList()
        for (syncUpError in syncUpErrors) {
            val syncUpErrorLog = SyncUpErrorLog()
            syncUpErrorLog.id = syncUpError.dateTime + "_" + syncUpError.operation + "_" + syncUpError.objectType
            syncUpErrorLog.dateTime = syncUpError.dateTime
            syncUpErrorLog.operation = syncUpError.operation
            syncUpErrorLog.request = syncUpError.request.toString()
            syncUpErrorLog.response = syncUpError.response.toString()
            syncUpErrorLog.fields = syncUpError.fields.toString()
            syncUpErrorLog.setErrorType(syncUpError.objectType)
            syncUpErrorLogs.add(syncUpErrorLog)
        }
        try {
            syncUpErrorLogRepository.upsertAll(syncUpErrorLogs)
        } catch (e: JSONException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    private fun logSyncDown() {
        val syncDownTarget = syncState.target as SyncDownTarget
        val syncDownErrors = syncDownTarget.syncDownErrors
        val syncDownErrorLogs: MutableList<SyncDownErrorLog> = ArrayList()
        for (syncDownError in syncDownErrors) {
            val syncDownErrorLog = SyncDownErrorLog()
            syncDownErrorLog.id = syncDownError.dateTime + "_" + syncState.soupName
            syncDownErrorLog.dateTime = syncDownError.dateTime
            syncDownErrorLog.query = if (syncDownError.query == null) "" else syncDownError.query
            syncDownErrorLog.request = if (syncDownError.request == null) "" else syncDownError.request.toString()
            syncDownErrorLog.response = if (syncDownError.response == null) "" else syncDownError.response.toString()
            syncDownErrorLog.setErrorType(syncState.soupName)
            syncDownErrorLog.operation = "DOWNLOAD"
            syncDownErrorLog.customErrorMessage = if (syncDownError.customErrorMessage == null) "" else syncDownError.customErrorMessage
            syncDownErrorLogs.add(syncDownErrorLog)
        }
        try {
            syncDownErrorLogRepository.upsertAll(syncDownErrorLogs)
        } catch (e: JSONException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    enum class SyncType {
        UP, DOWN
    }

}