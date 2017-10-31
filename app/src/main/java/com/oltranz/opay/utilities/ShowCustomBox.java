package com.oltranz.opay.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.utilities.views.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/25/2017.
 */

public class ShowCustomBox {
    private Context context;
    private HashMap<String, String> mDatas;
    private Dialog dialog;

    public ShowCustomBox(Context context, HashMap<String, String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    public void show() {
        if (mDatas.isEmpty())
            return;
        try {
            dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.custom_box);

            final Label boxTitle = (Label) dialog.findViewById(R.id.dialogTitle);
            ImageView close = (ImageView) dialog.findViewById(R.id.icClose);
            LinearLayout boxContent = (LinearLayout) dialog.findViewById(R.id.dialogContent);

            int fieldId = View.generateViewId();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            boxTitle.setText("TRANSACTION DETAILS");

            LinearLayoutCompat.LayoutParams lParam = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams contentParam;
            //setting box content
            for (Map.Entry entry : mDatas.entrySet()) {
                LinearLayout row = new LinearLayout(context);
                row.setLayoutParams(lParam);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));

                contentParam = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        1.0f);
                Label lbl = new Label(context);
                lbl.setText(entry.getKey().toString());
                lbl.setTextSize(12);
                lbl.setId(View.generateViewId());
                lbl.setLayoutParams(contentParam);
                lbl.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                lbl.setPadding(0,0,12,0);

                row.addView(lbl);

                Label lblValue = new Label(context);
                lblValue.setText(entry.getValue().toString());
                lblValue.setTextSize(15);
                lblValue.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                lblValue.setId(View.generateViewId());
                lblValue.setLayoutParams(contentParam);
                lblValue.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                row.addView(lblValue);

                boxContent.addView(row);
            }
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
