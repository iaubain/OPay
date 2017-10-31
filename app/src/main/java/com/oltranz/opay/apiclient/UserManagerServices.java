package com.oltranz.opay.apiclient;

import com.oltranz.opay.models.login.LoginRequest;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.models.signup.SignUpUserRequest;
import com.oltranz.opay.models.signup.SignUpUserResponse;
import com.oltranz.opay.models.signup.SignupResponse;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.user.UserProfile;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/23/2017.
 */

public interface UserManagerServices {
    String BASE_URL="http://opay.rw/8084/";
    String LOGIN_URL = "oltranz/services/usermanagement/login";
    String GET_USER = "oltranz/services/usermanagement/users/permissions/";
    String PROFILE_PIC = "oltranz/services/usermanagement/file/download";
    String CREATE_USER = "oltranz/services/usermanagement/public/users";

    @POST(LOGIN_URL)
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET(GET_USER+"{appId}")
    Call<UserDetails> getUserDetails(@Path("appId") String appId, @Query("access_token") String accessToken);

    @GET(PROFILE_PIC)
    Call<UserProfile> getProfilePicture(@Query("filename") String filePath, @Query("access_token") String accessToken);

    @POST(CREATE_USER)
    Call<SignUpUserResponse> signUpUser(@Body SignUpUserRequest request);
}
