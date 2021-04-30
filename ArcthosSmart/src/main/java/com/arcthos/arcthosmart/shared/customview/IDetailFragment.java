package io.topi.stoller.shared.customview;

import com.arcthos.arcthosmart.smartorm.SmartObject;

/**
 * ************************************************************
 * Autor : Rodrigo Augusto
 * Data : 29/09/2020
 * Empresa : TOPi
 * ************************************************************
 */
public interface IDetailFragment <T extends SmartObject> {
    void setupViewModel();
    void displayRecord(T record);
}