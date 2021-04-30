package com.arcthos.arcthosmart.shared.user.avatar;

import android.os.AsyncTask;

import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.mobilesync.app.MobileSyncSDKManager;
import com.salesforce.androidsdk.smartstore.store.SmartSqlHelper;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;
import com.arcthos.arcthosmart.shared.user.model.User;
import com.arcthos.arcthosmart.shared.user.model.repository.UserRepository;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class AvatarSynchronizer extends AsyncTask<Void, Void, Void> {

    private UserAccount userAccount;
    private SmartStore store;

    private User user;
    private UserRepository userRepository;

    private AvatarSynchronizerCallback avatarSynchronizerCallback;
    private AvatarProxy avatarProxy;


    public AvatarSynchronizer(UserAccount userAccount, AvatarSynchronizerCallback avatarSynchronizerCallback) {
        this.userAccount = userAccount;
        this.avatarSynchronizerCallback = avatarSynchronizerCallback;
    }

    private void execute() {
        setupService();
        getStore();
        getAvatar();
        syncFiles();
    }

    private void setupService() {
        avatarProxy = new AvatarProxy(userAccount, new AvatarCallback() {
            @Override
            public void onSuccess(ResponseBody responseBody, String userUrl) {
                handleSuccess(responseBody, userUrl);
            }

            @Override
            public void onFailure(Response<ResponseBody> response) {
                Timber.e("Failure to retrieve static avatar resource file: %s", response.toString());
                avatarSynchronizerCallback.onFinish();
            }

            @Override
            public void onException(Call<ResponseBody> call, Throwable t) {
                Timber.e(t, call.toString());
                avatarSynchronizerCallback.onFinish();
            }
        });
    }

    private void handleSuccess(ResponseBody responseBody, String avatarFile) {
        User userProfile = null;
        if (user.getFullPhotoUrl().equals(avatarFile)) {
            userProfile = user;
        }

        AvatarParser avatarParser = new AvatarParser(responseBody, userProfile);
        boolean parseSuccess = avatarParser.writeDownloadedFile();

        Timber.d(parseSuccess ? "Static avatar resource file downloaded successfully." : "Failure to download static avatar resource file.");

        AvatarOrganizer avatarOrganizer = new AvatarOrganizer();
        avatarOrganizer.execute();

        avatarSynchronizerCallback.onFinish();
    }

    private void getStore() {
        userAccount = MobileSyncSDKManager.getInstance().getUserAccountManager().getCurrentUser();
        store = MobileSyncSDKManager.getInstance().getSmartStore(userAccount);
    }

    private void getAvatar() {
        userRepository = new UserRepository(store);
        try {
            user = userRepository.findWithDeleteds(userAccount.getUserId());
        } catch (SmartSqlHelper.SmartSqlException e) {
            Timber.e(e);
        }
    }

    private void syncFiles() {
        if (user == null)
            return;

        avatarProxy.syncAvatar(user);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        execute();
        return null;
    }
}