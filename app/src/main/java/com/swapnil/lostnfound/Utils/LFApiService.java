package com.swapnil.lostnfound.Utils;

/**
 * Created by swapnil on 14/9/17.
 */

import com.swapnil.lostnfound.Models.Message;
import com.swapnil.lostnfound.Models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LFApiService {


    @FormUrlEncoded
    @POST("login/views/signup.php")
    Call<Message> userSignUp(@Field("name") String name,
                             @Field("email") String email,
                             @Field("password") String password);

    @POST("authenticate")
    Call<Message> userLogIn(@Body User user);

    /*@POST("authenticate")
    Call<Message> userLogIn(
            @Body FetchOTP fetchOTP,
            @HeaderMap Map<String, String> headers
    );*/



}
