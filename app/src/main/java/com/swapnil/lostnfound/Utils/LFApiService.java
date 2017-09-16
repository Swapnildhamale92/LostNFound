package com.swapnil.lostnfound.Utils;

/**
 * Created by swapnil on 14/9/17.
 */

import com.swapnil.lostnfound.Models.ChangeForgotPassword;
import com.swapnil.lostnfound.Models.ChangeForgotPasswordResponse;
import com.swapnil.lostnfound.Models.ForgotPasswordMail;
import com.swapnil.lostnfound.Models.ForgotPasswordMailResponse;
import com.swapnil.lostnfound.Models.LoginResponse;
import com.swapnil.lostnfound.Models.SignupResponse;
import com.swapnil.lostnfound.Models.UserLogin;
import com.swapnil.lostnfound.Models.UserSignUp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LFApiService {


    @POST("users")
    Call<SignupResponse> userSignUp(@Body UserSignUp userSignUp);

    @POST("authenticate")
    Call<LoginResponse> userLogIn(@Body UserLogin userLogin);

    @POST("users/forgotpassword")
    Call<ForgotPasswordMailResponse> forgotPassword(@Body ForgotPasswordMail forgotPasswordMail);

    @POST("users/forgotpassword")
    Call<ChangeForgotPasswordResponse> changePassword(@Body ChangeForgotPassword changeForgotPassword);

    /*@POST("authenticate")
    Call<LoginResponse> userLogIn(
            @Body FetchOTP fetchOTP,
            @HeaderMap Map<String, String> headers
    );*/



}
