package com.arcthos.arcthosmart.shared.user.avatar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
interface AvatarService {
    @GET
    Call<ResponseBody> getAvatar(@Url String userUrl);
}