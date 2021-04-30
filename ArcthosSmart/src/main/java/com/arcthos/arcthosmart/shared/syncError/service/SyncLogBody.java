package com.arcthos.arcthosmart.shared.syncError.service;

import java.io.Serializable;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 03/12/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class SyncLogBody implements Serializable {
    String body;

    public SyncLogBody(String body) {
        this.body = body;
    }
}
