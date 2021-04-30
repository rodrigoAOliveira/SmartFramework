package com.arcthos.arcthosmart.shared.user.avatar;

import java.io.File;
import java.io.IOException;


import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import timber.log.Timber;
import com.arcthos.arcthosmart.shared.user.model.User;

import static com.arcthos.arcthosmart.shared.user.avatar.AvatarOrganizer.AVATAR_FILE_PATH;
import static com.arcthos.arcthosmart.shared.user.avatar.AvatarOrganizer.AVATAR_PATH;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
class AvatarParser {
    private ResponseBody responseBody;
    private User userProfile;

    public AvatarParser(ResponseBody responseBody, User userProfile) {
        this.responseBody = responseBody;
        this.userProfile = userProfile;
    }

    public boolean writeDownloadedFile() {
        File salesforceAvatarFolder = new File(AVATAR_PATH);

        if (!salesforceAvatarFolder.exists()) {
            salesforceAvatarFolder.mkdirs();
        }

        File downloadedFile = new File(AVATAR_FILE_PATH);

        if (downloadedFile.exists())
            downloadedFile.delete();
        try {
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(downloadedFile));
            bufferedSink.writeAll(responseBody.source());
            bufferedSink.close();
            return true;
        } catch (IOException e) {
            Timber.e(e, "Failure to parse avatar file");
            return false;
        }
    }
}