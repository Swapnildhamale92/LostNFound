package com.swapnil.lostnfound.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by swapnil on 17/9/17.
 */

public class ChangeForgotPassword {

    @SerializedName("email")
    private String email;
    @SerializedName("temppass")
    private String tempPass;
    @SerializedName("password")
    private String password;

    public String getTempPass() {
        return tempPass;
    }

    public void setTempPass(String tempPass) {
        this.tempPass = tempPass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ChangeForgotPassword(String email, String tempPass, String password) {
        this.email = email;
        this.tempPass = tempPass;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
