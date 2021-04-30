package com.arcthos.arcthosmart.shared.customview;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dagger.android.support.DaggerAppCompatActivity;
import com.arcthos.arcthosmart.R;
import com.arcthos.arcthosmart.helper.MessageDialogHelper;

public class BaseActivity extends DaggerAppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private boolean doubleBackEnabled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!doubleBackEnabled || doubleBackToExitPressedOnce) {
                    finish();
                    return true;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void getErrorObserver(String error) {
        if (error == null) return;
        if (error.equals("")) return;

        MessageDialogHelper.showMessage(this, error);
    }

    public void enableDoubleBackToExit() {
        doubleBackEnabled = true;
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackEnabled || doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
