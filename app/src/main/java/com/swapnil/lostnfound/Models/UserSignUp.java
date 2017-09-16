package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 16/9/17.
 */

import com.google.gson.annotations.SerializedName;

public class UserSignUp {

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("location")
    private String location;
    @SerializedName("signup_lat")
    private String signup_lat;
    @SerializedName("signup_long")
    private String signup_long;
    @SerializedName("password")
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignup_lat() {
        return signup_lat;
    }

    public void setSignup_lat(String signup_lat) {
        this.signup_lat = signup_lat;
    }

    public String getSignup_long() {
        return signup_long;
    }

    public void setSignup_long(String signup_long) {
        this.signup_long = signup_long;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSignUp(String name, String email, String phone, String location, String signup_lat, String signup_long, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.signup_lat = signup_lat;
        this.signup_long = signup_long;

        this.password = password;
    }
}

