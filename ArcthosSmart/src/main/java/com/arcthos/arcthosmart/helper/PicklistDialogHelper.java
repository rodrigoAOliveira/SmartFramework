package com.arcthos.arcthosmart.helper;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.arcthos.arcthosmart.R;
import com.arcthos.arcthosmart.shared.picklist.Picklist;

import java.util.ArrayList;
import java.util.List;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 12/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class PicklistDialogHelper {

    private Context context;
    private List<Picklist> picklistValues;
    private PicklistListener picklistListener;

    private PicklistDialogHelper(Context context, List<Picklist> picklistValues, PicklistListener picklistListener) {
        this.context = context;
        this.picklistValues = picklistValues;
        this.picklistListener = picklistListener;
    }

    public static PicklistDialogHelper getInstance(Context context, List<Picklist> picklistValues, PicklistListener picklistListener) {
        return new PicklistDialogHelper(context, picklistValues, picklistListener);
    }

    public void show() {
        String[] values = new String[picklistValues.size()];
        for (int i = 0; i < picklistValues.size(); i++) {
            values[i] = picklistValues.get(i).getLabel();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.choose_an_option));
        builder.setItems(values, (dialog, which) -> picklistListener.onSelectItem(values[which], which));
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.show();
    }

    public void showSelectBox(List<String> options, boolean[] checkedItems) {
        CharSequence[] values = new CharSequence[options.size()];
        options.toArray(values);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.choose_an_option));
        builder.setMultiChoiceItems(values, checkedItems, (dialog, option, isChecked) -> {
            checkedItems[option] = isChecked;
            picklistListener.onSelectItem(String.valueOf(isChecked), option);
        });
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.show();
    }

    public void showWithoutValues(List<String> valuesToRemove) {
        List<String> valuesToShow = new ArrayList<>();
        for (Picklist picklist : picklistValues) {
            if (valuesToRemove.contains(picklist.getLabel())) {
                continue;
            }
            valuesToShow.add(picklist.getLabel());
        }

        String[] values = new String[valuesToShow.size()];
        for (int i = 0; i < valuesToShow.size(); i++) {

            values[i] = valuesToShow.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.choose_an_option));
        builder.setItems(values, (dialog, which) -> picklistListener.onSelectItem(values[which], which));
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.show();
    }
}