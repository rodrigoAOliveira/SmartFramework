package com.arcthos.arcthosmart.smartintegration;

import android.os.AsyncTask;

/**
 * Created by Vinicius Damiati on 23-Jul-18.
 */

public class SyncDownObjectTask  extends AsyncTask<Void, Void, Void> {

    private SObjectSyncher sObjectSyncher;

    public SyncDownObjectTask(SObjectSyncher sObjectSyncher) {
        this.sObjectSyncher = sObjectSyncher;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sObjectSyncher.syncDown(null);

        return null;
    }
}