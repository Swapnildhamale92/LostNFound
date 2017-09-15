package com.swapnil.lostnfound.Models;

/**
 * Created by swapnil on 14/9/17.
 */


public class Message {

    private Integer success;
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public Message() {
    }

    /**
     * @param message
     * @param success
     */
    public Message(Integer success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}