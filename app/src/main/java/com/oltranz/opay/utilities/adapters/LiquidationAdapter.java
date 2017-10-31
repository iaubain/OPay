package com.oltranz.opay.utilities.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oltranz.opay.R;
import com.oltranz.opay.models.history.HistoryLiquidation;
import com.oltranz.opay.utilities.ShowCustomBox;
import com.oltranz.opay.utilities.views.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/25/2017.
 */

public class LiquidationAdapter extends RecyclerView.Adapter<LiquidationAdapter.MyViewHolder> {
    private OnLiquidationHistoryAdapter mListener;
    private Context context;
    private List<HistoryLiquidation> mLiquidations;
    private List<HistoryLiquidation> tempList;
    private boolean addedList = false;
    private ProgressDialog progressDialog;

    public LiquidationAdapter(Context context, List<HistoryLiquidation> mLiquidations, OnLiquidationHistoryAdapter mListener) {
        this.mListener = mListener;
        this.context = context;
        this.mLiquidations = mLiquidations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.style_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HistoryLiquidation hLiquidation = mLiquidations.get(position);

        holder.txId.setText(hLiquidation.getTransactionId());
        holder.txTime.setText(hLiquidation.getDate());
        holder.customer.setText(hLiquidation.getMerchantTelephone());
        holder.amount.setText(hLiquidation.getAmount());

        if (hLiquidation.isLiquidated() && hLiquidation.isCompleted())
            holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_tx_success));
        else if (hLiquidation.isLiquidated() && !hLiquidation.isCompleted())
            holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_tx_pending));
        else
            holder.status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ico_tx_fails));

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show popup with all details
                HashMap<String, String> mData = hLiquidation.objectMap();
                if (!mData.isEmpty()) {
                    ShowCustomBox customBox = new ShowCustomBox(context, mData);
                    customBox.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLiquidations.size();
    }

    public void refreshAdapter(List<HistoryLiquidation> mLiquidations) {
        mLiquidations.clear();
        this.mLiquidations.addAll(mLiquidations);
        notifyDataSetChanged();
    }

    public void cleanAdapter() {
        mLiquidations.clear();
        notifyDataSetChanged();
    }

    public void populateAdapter(List<HistoryLiquidation> mLiquidations) {
        mLiquidations.clear();
        this.mLiquidations.addAll(mLiquidations);
        notifyDataSetChanged();
    }


    public void filter(String charText) {
        try {

            if (!addedList) {
                this.tempList = new ArrayList<>();
                this.tempList.addAll(mLiquidations);
                addedList = true;
            }

            charText = charText.toLowerCase(Locale.getDefault());
            mLiquidations.clear();
            if (charText.length() == 0) {
                mLiquidations.addAll(tempList);
                addedList = false;
            } else {
                for (HistoryLiquidation mLiquidation : tempList) {
                    if (mLiquidation.toString().toLowerCase(Locale.getDefault()).contains(charText)) {
                        mLiquidations.add(mLiquidation);
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mProgress(String message) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
        progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txId)
        Label txId;
        @BindView(R.id.txTime)
        Label txTime;
        @BindView(R.id.customer)
        Label customer;
        @BindView(R.id.amount)
        Label amount;
        @BindView(R.id.status)
        ImageView status;
        @BindView(R.id.overflow)
        ImageView overflow;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnLiquidationHistoryAdapter {
        void onLiquidationHistoryAdapter(boolean isInteraction, HistoryLiquidation historyLiquidation);
    }
}
