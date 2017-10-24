package com.oltranz.opay.utilities.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oltranz.opay.utilities.AppFont;

/**
 * Created by Hp on 6/8/2017.
 */

public class Label extends TextView {
    public Label(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Label(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(AppFont.provide(getContext()));
        }
    }
}
