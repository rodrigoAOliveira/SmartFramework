package com.arcthos.arcthosmart.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Vinicius Damiati on 26-Oct-17.
 */

public class ContentInflateHelper {
    public static void setupContent(Activity activity, FrameLayout frameLayout, int contentRes) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View stubView = inflater.inflate(contentRes, frameLayout, false);
        frameLayout.addView(stubView, lp);
    }
}
