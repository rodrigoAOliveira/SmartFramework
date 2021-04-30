package com.arcthos.arcthosmart.shared.syncError.manager

import android.os.Environment
import org.json.JSONException
import timber.log.Timber
import com.arcthos.arcthosmart.model.syncerror.SyncUpErrorLog
import com.arcthos.arcthosmart.network.syncError.SyncUpErrorLogRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*


class SyncUpErrorLogManager(private val syncUpErrorLogRepository: SyncUpErrorLogRepository) {

    private var syncUpErrorLogs: MutableList<SyncUpErrorLog>? = null

    fun generateSyncUpInternalLog() {

        val file = File(Environment
                .getExternalStorageDirectory().path
                + "/SalesforceApplication/ErrorLogs/CurrentSyncUpErrorLog.txt")

        if (!file.exists())
            return

        syncUpErrorLogs = ArrayList()

        try {
            val br = BufferedReader(FileReader(file))
            var line: String
            while (br.readLine().also { line = it } != null) {
                if (line == "") continue
                try {
                    (syncUpErrorLogs as ArrayList<SyncUpErrorLog>).add(parseLine(line))
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
            br.close()
            saveData()
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    private fun parseLine(line: String): SyncUpErrorLog {
        val splittedLine = line.split(" \\|\\|-\\|\\| ".toRegex()).toTypedArray()
        val dateTime = splittedLine[0].trim { it <= ' ' }
        val operation = splittedLine[1].trim { it <= ' ' }
        val sObject = splittedLine[2].split(":".toRegex()).toTypedArray()[0]
        val response = splittedLine[2].substring(sObject.length + 2, splittedLine[2].length)
        val fields = splittedLine[3].substring(16, splittedLine[3].length)
        val syncUpErrorLog = SyncUpErrorLog()
        syncUpErrorLog.id = dateTime + "_" + operation + "_" + sObject
        syncUpErrorLog.dateTime = dateTime
        syncUpErrorLog.operation = operation
        syncUpErrorLog.setErrorType(sObject)
        syncUpErrorLog.response = response
        syncUpErrorLog.fields = fields
        return syncUpErrorLog
    }

    private fun saveData() {
        try {
            syncUpErrorLogRepository.upsertAll(syncUpErrorLogs)
        } catch (e: JSONException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

}