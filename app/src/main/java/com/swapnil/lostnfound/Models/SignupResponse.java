package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 16/9/17.
 */

public class SignupResponse {

    private Integer code;
    private String message;


    /**
     * No args constructor for use in serialization
     */
    public SignupResponse() {
    }

    /**
     * @param message
     * @param code
     */
    public SignupResponse(Integer code, String message ) {
        super();
        this.code = code;
        this.message = message;
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

}
