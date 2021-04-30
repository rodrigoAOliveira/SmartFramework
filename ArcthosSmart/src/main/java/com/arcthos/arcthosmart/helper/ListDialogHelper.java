package com.arcthos.arcthosmart.helper;

import android.app.AlertDialog;
import android.content.Context;

import com.arcthos.arcthosmart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius Damiati on 10-Jan-18.
 */

public class ListDialogHelper {
    private Context context;
    private List<String> listValues;
    private PicklistListener picklistListener;

    private ListDialogHelper(Context context, List<String> listValues, PicklistListener picklistListener) {
        this.context = context;
        this.listValues = listValues;
        this.picklistListener = picklistListener;
    }

    public static ListDialogHelper getInstance(Context context, List<String> listValues, PicklistListener picklistListener) {
        ListDialogHelper listDialogHelper = new ListDialogHelper(context, listValues, picklistListener);
        return  listDialogHelper;
    }

    public void show() {
        String[] values = new String[this.listValues.size()];
        for(int i = 0; i < this.listValues.size(); i++) {
            values[i] = this.listValues.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.choose_an_option));
        builder.setItems(values, (dialog, which) -> {
            picklistListener.onSelectItem(values[which], which);
        });
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.show();
    }

    public void showWithoutValues(List<String> valuesToRemove) {
        List<String> valuesToShow = new ArrayList<>();
        for(String picklist : listValues) {
            if(valuesToRemove.contains(picklist)) {
                continue;
            }
            valuesToShow.add(picklist);
        }

        String[] values = new String[valuesToShow.size()];
        for(int i = 0; i < valuesToShow.size(); i++) {

            values[i] = valuesToShow.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.choose_an_option));
        builder.setItems(values, (dialog, which) -> {
            picklistListener.onSelectItem(values[which], which);
        });
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.show();
    }
}
