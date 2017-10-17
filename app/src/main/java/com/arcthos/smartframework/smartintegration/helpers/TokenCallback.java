package com.arcthos.smartframework.smartintegration.helpers;

import okhttp3.ResponseBody;

public interface TokenCallback {
    void onSuccess(RefreshToken refreshToken);

    void noConnection();

    void onFailure(ResponseBody errorBody);

    void onException(Throwable t);
}
