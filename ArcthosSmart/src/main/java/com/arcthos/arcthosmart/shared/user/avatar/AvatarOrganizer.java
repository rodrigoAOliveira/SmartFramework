package com.arcthos.arcthosmart.shared.user.avatar;

import android.os.Environment;

import java.io.File;

import timber.log.Timber;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
class AvatarOrganizer extends FileOrganizer {
    static final String AVATAR_PATH = Environment
            .getExternalStorageDirectory().getPath() + File.separator + "Salesforce" + File.separator + "Stoller" + File.separator + "Image";
    static final String AVATAR_FILE_PATH = AVATAR_PATH + File.separator + "profilephoto.bmp";
    private static final String DELETE_AVATAR_PATH = Environment
            .getExternalStorageDirectory() + File.separator + "Salesforce" + File.separator + "Stoller" + File.separator + "Image";

    @Override
    protected boolean checkDownloadedFile() {
        File downloadedFile = new File(AVATAR_FILE_PATH);
        if (downloadedFile.exists()) {
            return false;
        }
        return false;
    }

    @Override
    protected void deleteOldFile() {
        File dir = new File(DELETE_AVATAR_PATH);
        if (!dir.exists()) {
            Timber.d("No old files to delete");
            return;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                new File(dir, aChildren).delete();
            }
        }

        dir.delete();
        Timber.d("Old files deleted");
    }
}