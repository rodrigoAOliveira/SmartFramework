package com.arcthos.arcthosmart.shared.viewmodel;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.arcthos.arcthosmart.R;

import static com.arcthos.arcthosmart.helper.DoubleHelper.formatCurrency;
import static com.arcthos.arcthosmart.helper.DoubleHelper.formatPercentage;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 22/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class BaseViewModel extends AndroidViewModel {
    protected Resources resources;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.resources = application.getResources();
    }

    protected Resources getResources() {
        return resources;
    }

    public String getMoneyValue(String value) {
        if (value != null && !value.isEmpty()) {
            return resources.getString(R.string.tv_money, formatCurrency(value));
        } else {
            return "";
        }
    }

    public String getPercentageValue(String value) {
        if (value != null && !value.isEmpty()) {
            return resources.getString(R.string.tv_percentage, formatPercentage(value));
        } else {
            return "";
        }
    }

    public String getWeightValue(String value) {
        if (value != null && !value.isEmpty()) {
            return resources.getString(R.string.tv_weight_kg, formatPercentage(value));
        } else {
            return "";
        }
    }
}
