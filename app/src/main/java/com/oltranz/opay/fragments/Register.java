package com.oltranz.opay.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.apiclient.MerchantServices;
import com.oltranz.opay.models.signup.SignUpUserRequest;
import com.oltranz.opay.models.signup.SignUpUserResponse;
import com.oltranz.opay.models.signup.registermerchant.MerchantContact;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantRequest;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantResponse;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.loader.RegisterMerchantLoader;
import com.oltranz.opay.utilities.loader.RegisterUserLoader;
import com.oltranz.opay.utilities.views.EditHelper;
import com.oltranz.opay.utilities.views.MButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRegisterListener} interface
 * to handle interaction events.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment implements RegisterUserLoader.RegisterInteraction,
        RegisterMerchantLoader.OnMerchantRegisterLoader {
    @BindView(R.id.register)
    MButton register;
    @BindView(R.id.bName)
    EditHelper bName;
    @BindView(R.id.bTin)
    EditHelper bTin;
    @BindView(R.id.bAddress)
    EditHelper address;
    @BindView(R.id.contactName)
    EditHelper contact;
    @BindView(R.id.contactEmail)
    EditHelper email;
    @BindView(R.id.contactPhone)
    EditHelper phone;

    private SignUpUserResponse response;
    private SignUpUserRequest request;
    private RegisterMerchantRequest merchantRequest;
    private RegisterMerchantResponse merchantResponse;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    private Dialog mainDialog;

    private OnRegisterListener mListener;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Register.
     */
    public static Register newInstance() {
        Register fragment = new Register();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bName.getText().toString())) {
                    bName.setError("Invalid business");
                } else if (TextUtils.isEmpty(bTin.getText().toString())) {
                    bName.setError("Invalid TIN");
                } else if (TextUtils.isEmpty(address.getText().toString())) {
                    address.setError("Invalid address");
                } else if (TextUtils.isEmpty(contact.getText().toString())) {
                    contact.setError("Invalid name");
                } else if (TextUtils.isEmpty(email.getText().toString()) || !DataFactory.isValidEmail(email.getText().toString())) {
                    email.setError("Invalid email");
                } else if (TextUtils.isEmpty(phone.getText().toString()) || !DataFactory.isValidPhone(phone.getText().toString())) {
                    phone.setError("Invalid phone");
                } else {
                    termsAndConditions();
                }
            }
        });
    }

    public void termsAndConditions() {
        try {
            mainDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            mainDialog.setCancelable(false);
            mainDialog.setCanceledOnTouchOutside(false);
            mainDialog.setContentView(R.layout.terms);

            MButton accept = (MButton) mainDialog.findViewById(R.id.ok);
            MButton cancel = (MButton) mainDialog.findViewById(R.id.cancel);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainDialog.dismiss();
                    mProgress("Creating super user");
                    request = new SignUpUserRequest(email.getText().toString(),
                            contact.getText().toString(),
                            contact.getText().toString(),
                            phone.getText().toString());

                    merchantRequest = new RegisterMerchantRequest("",
                            contact.getText().toString(),
                            bName.getText().toString(),
                            address.getText().toString(),
                            email.getText().toString(),
                            "http://www.oltranz.com/",
                            bTin.getText().toString(),
                            new MerchantContact(contact.getText().toString(),phone.getText().toString(), email.getText().toString()),
                            new MerchantContact(contact.getText().toString(),phone.getText().toString(), email.getText().toString()));

                    RegisterUserLoader registerUserLoader = new RegisterUserLoader(Register.this,
                            request);
                    registerUserLoader.startLoading();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainDialog.dismiss();
                }
            });

            mainDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            if (mainDialog != null && mainDialog.isShowing())
                mainDialog.dismiss();
        }
    }

    private void popBox(String message) {
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(R.string.dialog_title);
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshMerchantCreation(String message) {
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(R.string.dialog_title);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    mProgress("Registering merchant");
                    RegisterMerchantLoader registerMerchantLoader = new RegisterMerchantLoader(Register.this, "NO_TOKEN", merchantRequest);
                    registerMerchantLoader.startLoading();
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void mProgress(String message) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterListener) {
            mListener = (OnRegisterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRegistration(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded){
            popBox(object+"");
        }else{
            this.response = (SignUpUserResponse) object;
            mProgress("Registering merchant");
            merchantRequest.setInitialUserId(response.getBody().getId());
            RegisterMerchantLoader registerMerchantLoader = new RegisterMerchantLoader(Register.this, MerchantServices.SIGNATURE, merchantRequest);
            registerMerchantLoader.startLoading();
        }
    }

    @Override
    public void onRegisterMerchant(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded){
            //reload the process
            refreshMerchantCreation(object+"");
        }else{
            if(mListener != null){
                mListener.onRegister(true, response, "Success");
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterListener {
        void onRegister(boolean isRegistration, SignUpUserResponse registrationResponse, Object extra);
    }
}
