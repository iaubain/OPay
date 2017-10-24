package com.oltranz.opay.utilities.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.oltranz.opay.utilities.AppFont;


/**
 * Created by Hp on 6/8/2017.
 */

public class MButton extends Button {
    public MButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(AppFont.provide(getContext()));
        }
    }
}
