package com.arcthos.arcthosmart.smartintegration;

import android.os.AsyncTask;

/**
 * Created by Vinicius Damiati on 06-Mar-18.
 */

public class SyncObjectTask extends AsyncTask<Void, Void, Void> {

    private SObjectSyncher sObjectSyncher;

    public SyncObjectTask(SObjectSyncher sObjectSyncher) {
        this.sObjectSyncher = sObjectSyncher;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(sObjectSyncher.hasSoup()) {
            sObjectSyncher.syncUpAndDown();
        } else {
            sObjectSyncher.syncDown();
        }

        return null;
    }
}
