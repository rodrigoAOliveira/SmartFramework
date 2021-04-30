package com.arcthos.arcthosmart.shared.syncError.helper;

import com.salesforce.androidsdk.accounts.UserAccount;

import java.util.Date;
import java.util.List;

import com.arcthos.arcthosmart.helper.DateHelper;
import com.arcthos.arcthosmart.model.syncerror.SyncDownErrorLog;
import com.arcthos.arcthosmart.model.syncerror.SyncUpErrorLog;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 04/12/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class SyncErrorStringHelper {
    public static String getFileName(UserAccount userAccount) {
        return String.format("%s_%s_%s.html", DateHelper.convertToLogDateTime(new Date()), getInstance(userAccount), userAccount.getUsername());
    }

    public static String getStringToSend(List<SyncUpErrorLog> syncUpErrorLogs, List<SyncDownErrorLog> syncDownErrorLogs) {
        StringBuilder toSend = new StringBuilder();
        addHeader(toSend);
        toSend.append("*****************************************************************************\n");
        getSyncDownErrorLogToSend(toSend, syncDownErrorLogs);
        getSyncUpErrorLogToSend(toSend, syncUpErrorLogs);
        toSend.append("*****************************************************************************\n");
        addFooter(toSend);
        return toSend.toString();
    }

    private static String getInstance(UserAccount userAccount) {
        if (userAccount.getLoginServer().contains("test")) {
            return "SANDBOX";
        } else {
            return "PRODUCTION";
        }
    }

    private static void addHeader(StringBuilder toSend) {
        toSend.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "    <style type=\"text/css\">\n" +
                "    span.label { color:#80ccff;font-weight:bold; }\n" +
                "    span.title { color:#ff0000;font-weight:bold; }\n" +
                "    </style>" +
                "<head>\n" +
                "    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n" +
                "</head>\n" +
                "<body style='background-color:black'>\n" +
                "<pre style='word-wrap: break-word; white-space: pre-wrap; color: #ffff80'>");
    }

    private static void addFooter(StringBuilder toSend) {
        toSend.append("</pre>\n" +
                "</body>\n" +
                "</html>");
    }

    private static void getSyncDownErrorLogToSend(StringBuilder toSend, List<SyncDownErrorLog> syncDownErrorLogs) {
        if (syncDownErrorLogs == null || syncDownErrorLogs.isEmpty()) return;
        toSend.append("*****************************************************************************\n");
        toSend.append(titleHeader()).append("SyncDownError\n").append(titleFooter());
        toSend.append("-----------------------------------------------------------------------------\n");
        for (SyncDownErrorLog syncDownErrorLog : syncDownErrorLogs) {
            buildSyncDownErrorLogToSend(toSend, syncDownErrorLog);
        }
        toSend.append("*****************************************************************************\n");
    }

    private static String titleHeader() {
        return "<span class=\"title\">";
    }

    private static String titleFooter() {
        return "</span>";
    }

    private static void buildSyncDownErrorLogToSend(StringBuilder toSend, SyncDownErrorLog syncDownErrorLog) {
        toSend.append(labelHeader()).append("SObject: ").append(labelFooter()).append(syncDownErrorLog.getErrorType()).append(lineEnd());
        toSend.append(labelHeader()).append("DateTime: ").append(labelFooter()).append(syncDownErrorLog.getDateTime()).append(lineEnd());
        toSend.append(labelHeader()).append("Query: ").append(labelFooter()).append(syncDownErrorLog.getQuery()).append(lineEnd());
        toSend.append(labelHeader()).append("Request: ").append(labelFooter()).append(syncDownErrorLog.getRequest()).append(lineEnd());
        toSend.append(labelHeader()).append("Response: ").append(labelFooter()).append(replaceLineBreak(syncDownErrorLog.getResponse())).append(lineEnd());
        toSend.append(labelHeader()).append("CustomErrorMessage: ").append(labelFooter()).append(syncDownErrorLog.getCustomErrorMessage()).append(lineEnd());
        toSend.append("-----------------------------------------------------------------------------\n");
    }

    private static String labelHeader() {
        return "<span class=\"label\">";
    }

    private static String labelFooter() {
        return "</span>";
    }

    private static String lineEnd() {
        return "\n";
    }

    private static String replaceLineBreak(String response) {
        return response.replaceAll("\\\\n", "<br/>");
    }

    private static void getSyncUpErrorLogToSend(StringBuilder toSend, List<SyncUpErrorLog> syncUpErrorLogs) {
        if (syncUpErrorLogs == null || syncUpErrorLogs.isEmpty()) return;
        toSend.append("*****************************************************************************\n");
        toSend.append("SyncUpError\n");
        for (SyncUpErrorLog syncUpErrorLog : syncUpErrorLogs) {
            buildSyncUpErrorLogToSend(toSend, syncUpErrorLog);
        }
        toSend.append("*****************************************************************************\n");
    }

    private static void buildSyncUpErrorLogToSend(StringBuilder toSend, SyncUpErrorLog syncUpErrorLog) {
        toSend.append("-----------------------------------------------------------------------------\n");
        toSend.append(labelHeader()).append("SObject: ").append(labelFooter()).append(syncUpErrorLog.getErrorType()).append(lineEnd());
        toSend.append(labelHeader()).append("DateTime: ").append(labelFooter()).append(syncUpErrorLog.getDateTime()).append(lineEnd());
        toSend.append(labelHeader()).append("Fields: ").append(labelFooter()).append(syncUpErrorLog.getFields()).append(lineEnd());
        toSend.append(labelHeader()).append("Operation: ").append(labelFooter()).append(syncUpErrorLog.getOperation()).append(lineEnd());
        toSend.append(labelHeader()).append("Request: ").append(labelFooter()).append(syncUpErrorLog.getRequest()).append(lineEnd());
        toSend.append(labelHeader()).append("Response: ").append(labelFooter()).append(replaceLineBreak(syncUpErrorLog.getResponse())).append(lineEnd());
        toSend.append("-----------------------------------------------------------------------------\n");
    }
}
