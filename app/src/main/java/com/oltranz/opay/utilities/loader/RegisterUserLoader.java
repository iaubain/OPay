package com.oltranz.opay.utilities.loader;

import android.util.Log;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.UserManagerServices;
import com.oltranz.opay.models.signup.SignUpUserRequest;
import com.oltranz.opay.models.signup.SignUpUserResponse;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 6/9/2017.
 */

public class RegisterUserLoader {
    private RegisterInteraction mListener;
    private SignUpUserRequest request;
    private String message;
    private SignUpUserResponse signupResponse = null;

    public RegisterUserLoader(RegisterInteraction mListener, SignUpUserRequest request) {
        this.mListener = mListener;
        this.request = request;
    }

    public void startLoading() {
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface RegisterInteraction {
        void onRegistration(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                Log.d("Register", DataFactory.objectToString(request));
                UserManagerServices userService = ServiceGen.createService(UserManagerServices.class, UserManagerServices.BASE_URL);
                Call<SignUpUserResponse> callService = userService.signUpUser(request);
                callService.enqueue(new Callback<SignUpUserResponse>() {
                    @Override
                    public void onResponse(Call<SignUpUserResponse> call, Response<SignUpUserResponse> response) {
                        int statusCode = response.code();
                        Log.d("Register", DataFactory.objectToString(response.body()));
                        if (statusCode == 500) {
                            message = "Internal server error";
                            onPostExecute(signupResponse);
                            return;
                        } else if (statusCode != 200 && statusCode != 201) {
                            message = response.message();
                            onPostExecute(signupResponse);
                            return;
                        }

                        if (response.body() == null) {
                            message = "Empty server response";
                            onPostExecute(signupResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body" : "Response with invalid body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<SignUpUserResponse> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(signupResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(signupResponse);
            }
        }

        protected void onPostExecute(SignUpUserResponse signupResponse) {
            try {
                if (signupResponse == null)
                    mListener.onRegistration(false, message);
                else {
                    if (signupResponse.getCode() != null) {
                        if (!signupResponse.getCode().equals("200"))
                            mListener.onRegistration(false, signupResponse.getDescription());
                        else
                            mListener.onRegistration(true, signupResponse);
                    } else {
                        mListener.onRegistration(false, "Registration error. " + message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mListener.onRegistration(false, "Error: " + e.getMessage());
            }

        }
    }
}
