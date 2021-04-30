package com.arcthos.arcthosmart.shared.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.arcthos.arcthosmart.R;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 27/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class DrawerTextView extends LinearLayout {
    private TextView text;

    public DrawerTextView(Context context) {
        super(context);
        init(context);
    }

    public DrawerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawerTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_drawer_text_view, this);
        text = view.findViewById(R.id.tv_text_drawer_text_view);
    }

    public void setText(String text) {
        this.text.setText(text);
    }
}
