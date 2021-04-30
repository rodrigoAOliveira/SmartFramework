package com.arcthos.arcthosmart.shared.syncError.service;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 03/12/2018
 * Empresa : TOPi
 * ************************************************************
 */
public interface SyncLogInterface {
    @POST("/premix-logs/upload.php")
    Single<SyncLogResponse> sendSyncError(@Header("file") String file, @Body RequestBody body);
}
