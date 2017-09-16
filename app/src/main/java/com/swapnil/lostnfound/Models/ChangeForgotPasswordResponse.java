package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 17/9/17.
 */

public class ChangeForgotPasswordResponse {
    private String message;


    /**
     * No args constructor for use in serialization
     */
    public ChangeForgotPasswordResponse() {
    }

    /**
     * @param message
     */
    public ChangeForgotPasswordResponse(String message ) {
        super();
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
