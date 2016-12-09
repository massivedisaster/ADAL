package com.massivedisaster.adal.network;

public class APIError {

    private String mMessage;
    private int code;

    public APIError(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getCode() {
        return code;
    }
}
