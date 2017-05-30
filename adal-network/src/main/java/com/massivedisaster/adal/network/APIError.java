/*
 * ADAL - A set of Android libraries to help speed up Android development.
 * Copyright (C) 2017 ADAL.
 *
 * ADAL is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * ADAL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with ADAL. If not, see <http://www.gnu.org/licenses/>.
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
