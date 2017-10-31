package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.UserManagerServices;
import com.oltranz.opay.models.user.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class ProfileLoader {
    private String accessToken;
    private String filePath;
    private OnProfileLoader mListener;
    private String message;
    private UserProfile userProfile = null;

    public ProfileLoader(String accessToken, String filePath, OnProfileLoader mListener) {
        this.accessToken = accessToken;
        this.filePath = filePath;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnProfileLoader {
        void onProfileLoader(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                UserManagerServices userService = ServiceGen.createService(UserManagerServices.class, UserManagerServices.BASE_URL);
                Call<UserProfile> callService = userService.getProfilePicture(filePath,accessToken);
                callService.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        int statusCode = response.code();

                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(userProfile);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(userProfile);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(userProfile);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(userProfile);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(userProfile);
            }
        }

        protected void onPostExecute(UserProfile userProfile) {
            try{
                if(userProfile == null)
                    mListener.onProfileLoader(false, message);
                else{
                    if(userProfile.getFile() != null){
                        mListener.onProfileLoader(true, userProfile);
                    }else{
                        mListener.onProfileLoader(false, "Profile error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onProfileLoader(false, "Error: "+e.getMessage());
            }

        }
    }
}
