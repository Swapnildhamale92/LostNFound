package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 14/9/17.
 */


public class LoginResponse {

    private Integer code;
    private String message;
    private String token;
    private String userid;


    /**
     * No args constructor for use in serialization
     */
    public LoginResponse() {
    }

    /**
     * @param message
     * @param code
     * @param userid
     * @param token
     */
    public LoginResponse(Integer code, String message , String userid , String token) {
        super();
        this.code = code;
        this.message = message;
        this.userid = userid;
        this.token = token;
    }

    public Integer getSuccessCode() {
        return code;
    }

    public void setSuccessCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userid) {
        this.userid = userid;
    }
}