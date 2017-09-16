package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 17/9/17.
 */

public class ForgotPasswordMailResponse {

    private String message;


    /**
     * No args constructor for use in serialization
     */
    public ForgotPasswordMailResponse() {
    }

    /**
     * @param message
     */
    public ForgotPasswordMailResponse(String message ) {
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
