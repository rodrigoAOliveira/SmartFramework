package com.arcthos.smartframework.smartintegration;

import android.content.Context;
import android.os.AsyncTask;

import com.arcthos.smartframework.smartorm.SmartObject;
import com.salesforce.androidsdk.smartsync.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public abstract class BaseGeneralSync extends AsyncTask<Void, Void, Void> {

    public static final String LAST_SYNC = "lastSync";

    private Context context;
    private SyncCallback syncCallback;
    private String formattedLastUpdate;

    public BaseGeneralSync(Context context, SyncCallback syncCallback) {
        this.context = context;
        this.syncCallback = syncCallback;
        PreferencesManager.initialize(context);
    }

    private String formatLastUpdate(String lastUpdate) {
        Locale locale = context.getResources().getConfiguration().locale;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale);

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date lastUpdateDate;
        try {
            lastUpdateDate = sdf.parse(lastUpdate);
        } catch (ParseException e) {
            lastUpdateDate = new Date(0);
        }

        return sdf2.format(lastUpdateDate);
    }

    private synchronized void performSync() {
        String lastUpdate = PreferencesManager.getInstance().getStringValue(LAST_SYNC);
        formattedLastUpdate = formatLastUpdate(lastUpdate);

        syncObjects();
        setLastDateUpdate();
    }

    protected abstract void syncObjects();

    private void setLastDateUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        PreferencesManager.getInstance().setValue(LAST_SYNC, sdf.format(new Date()));
    }

    protected void syncObject(Class<? extends SmartObject> model) {
        SObjectSyncher SObjectSyncher = new SObjectSyncher(model, context, syncCallback);
        String where = Constants.LAST_MODIFIED_DATE + ">" + formattedLastUpdate;
        SObjectSyncher.setWhere(where);

        if(SObjectSyncher.hasSoup()) {
            SObjectSyncher.syncUpAndDown();
        } else {
            SObjectSyncher.syncDown();
        }
    }

    public String getFormattedLastUpdate() {
        return formattedLastUpdate;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        performSync();
        return null;
    }
}
