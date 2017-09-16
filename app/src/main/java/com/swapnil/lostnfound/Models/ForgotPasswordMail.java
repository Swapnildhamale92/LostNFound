package com.swapnil.lostnfound.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by swapnil on 16/9/17.
 */

public class ForgotPasswordMail {

    @SerializedName("email")
    private String email;

    public ForgotPasswordMail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
