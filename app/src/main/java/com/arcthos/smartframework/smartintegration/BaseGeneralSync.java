package com.arcthos.smartframework.smartintegration;

import android.content.Context;
import android.os.AsyncTask;

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

    private Context context;
    private SyncCallback syncCallback;
    private String formattedLastUpdate;

    public BaseGeneralSync(Context context, SyncCallback syncCallback) {
        this.context = context;
        this.syncCallback = syncCallback;
        PreferencesManager.initialize(context);
    }

    private String formatLastUpdate(String lastUpdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("PT", "BR"));

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
        String lastUpdate = PreferencesManager.getInstance().getStringValue("lastUpdate");
        formattedLastUpdate = formatLastUpdate(lastUpdate);

        syncObjects();
        setLastDateUpdate();
    }

    protected abstract void syncObjects();

    private void setLastDateUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        PreferencesManager.getInstance().setValue("lastUpdate", sdf.format(new Date()));
    }

    protected void syncObject(Class<?> model) {
        SObjectSyncher SObjectSyncher = new SObjectSyncher(model, syncCallback);
        String where = Constants.LAST_MODIFIED_DATE + ">" + formattedLastUpdate;
        SObjectSyncher.setWhere(where);

        if(SObjectSyncher.hasSoup()) {
            SObjectSyncher.syncDown();
        } else {
            SObjectSyncher.syncUpAndDown();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        performSync();
        return null;
    }
}
