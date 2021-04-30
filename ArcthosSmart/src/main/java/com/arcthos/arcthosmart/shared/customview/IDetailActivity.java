package io.topi.stoller.shared.customview;

import com.arcthos.arcthosmart.smartorm.SmartObject;

public interface IDetailActivity<T extends SmartObject> {
    void setupToolbar();
    void setupViewModel();
    void displayRecord(T record);
}
