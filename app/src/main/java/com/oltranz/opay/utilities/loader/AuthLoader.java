package com.oltranz.opay.utilities.loader;

import android.content.Context;
import android.util.Log;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.UserManagerServices;
import com.oltranz.opay.models.login.LoginRequest;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 6/9/2017.
 */

public class AuthLoader {
    private AuthLoaderInteraction mListener;
    private Context context;
    private LoginRequest loginRequest;
    private String message;
    private LoginResponse loginResponse = null;

    public AuthLoader(AuthLoaderInteraction mListener, Context context, LoginRequest loginRequest) {
        this.mListener = mListener;
        this.context = context;
        this.loginRequest = loginRequest;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface AuthLoaderInteraction{
        void onAuthLoader(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                UserManagerServices userService = ServiceGen.createService(UserManagerServices.class, UserManagerServices.BASE_URL);
                Call<LoginResponse> callService = userService.login(loginRequest);
                callService.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        int statusCode = response.code();
                        Log.d("Auth", DataFactory.objectToString(response.body()));
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(loginResponse);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(loginResponse);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(loginResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with invalid body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(loginResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(loginResponse);
            }
        }

        protected void onPostExecute(LoginResponse loginResponse) {
            try{
                if(loginResponse == null)
                    mListener.onAuthLoader(false, message);
                else{
                    if(loginResponse.getAccess_token() != null){
                        mListener.onAuthLoader(true, loginResponse);
                    }else{
                        mListener.onAuthLoader(false, "Authentication error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onAuthLoader(false, "Error: "+e.getMessage());
            }

        }
    }
}
