package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.UserManagerServices;
import com.oltranz.opay.models.user.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class UserDetailsLoader {
    private String accessToken;
    private String appId;
    private OnUserDetails mListener;
    private String message;
    private UserDetails userDetails = null;

    public UserDetailsLoader(String accessToken, String appId, OnUserDetails mListener) {
        this.accessToken = accessToken;
        this.appId = appId;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnUserDetails{
        void onUserDetails(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                UserManagerServices userService = ServiceGen.createService(UserManagerServices.class, UserManagerServices.BASE_URL);
                Call<UserDetails> callService = userService.getUserDetails(appId,accessToken);
                callService.enqueue(new Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                        int statusCode = response.code();

                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(userDetails);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(userDetails);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(userDetails);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure.";
                        onPostExecute(userDetails);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(userDetails);
            }
        }

        protected void onPostExecute(UserDetails userDetails) {
            try{
                if(userDetails == null)
                    mListener.onUserDetails(false, message);
                else{
                    if(userDetails.getUser() != null){
                        mListener.onUserDetails(true, userDetails);
                    }else{
                        mListener.onUserDetails(false, "User details error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onUserDetails(false, "Error: "+e.getMessage());
            }

        }
    }
}
