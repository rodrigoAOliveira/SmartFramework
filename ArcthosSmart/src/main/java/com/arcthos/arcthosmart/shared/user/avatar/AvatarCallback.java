package com.arcthos.arcthosmart.shared.user.avatar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
interface AvatarCallback {
    void onSuccess(ResponseBody responseBody, String userUrl);

    void onFailure(Response<ResponseBody> response);

    void onException(Call<ResponseBody> call, Throwable t);
}
