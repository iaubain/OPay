package com.oltranz.opay.utilities.views;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.google.common.base.Optional;
import com.oltranz.opay.utilities.AppFont;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 9/25/2017.
 */

public class EditHelper extends TextInputEditText {
    public EditHelper(Context context) {
        super(context);
        init();
    }

    public EditHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(AppFont.provide(getContext()));
        }
    }
}
