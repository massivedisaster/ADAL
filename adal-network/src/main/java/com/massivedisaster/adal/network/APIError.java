/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.adal.network;

/**
 * Represents a API error.
 */
public class APIError {

    private final String mMessage;
    private int mCode;

    /**
     * Constructs {@link APIError} instance.
     *
     * @param code    the code.
     * @param message the menssage.
     */
    public APIError(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    /**
     * Constructs {@link APIError} instance.
     *
     * @param message the menssage
     */
    public APIError(String message) {
        mMessage = message;
    }

    /**
     * Gets the message of the error.
     *
     * @return the error message.
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Gets the error code.
     *
     * @return the error code.
     */
    public int getErrorCode() {
        return mCode;
    }
}
