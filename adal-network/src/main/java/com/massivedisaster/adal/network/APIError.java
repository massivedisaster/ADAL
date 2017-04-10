package com.massivedisaster.adal.network;

public class APIError {

    private String mMessage;
    private int mCode;

    public APIError(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    public APIError(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getErrorCode() {
        return mCode;
    }
}
