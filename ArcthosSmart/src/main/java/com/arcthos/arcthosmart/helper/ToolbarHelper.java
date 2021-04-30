package com.arcthos.arcthosmart.helper;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arcthos.arcthosmart.R;

/**
 * Created by Vinicius Damiati on 26-Dec-17.
 */

public class ToolbarHelper {
    private AppCompatActivity activity;
    private Toolbar toolbar;

    public ToolbarHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setupToolbar(int toolbarId, String title) {
        View view = activity.findViewById(toolbarId);

        toolbar = view.findViewById(R.id.appToolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.white));
        activity.setSupportActionBar(toolbar);
    }

    public void displayBack() {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
