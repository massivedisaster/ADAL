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

package com.massivedisaster.adal.connectivity;

/** Utility class meant to hold all constants for the Network sub module. */
@SuppressWarnings({"unused", "WeakerAccess"}) public class NetworkConstants {

    /** ########## Misc. ########## */
    static final String LOG_TAG = "CADL-Network";

    /** ########## Exception Messages ########## */
    static final String ASSERTION_ERROR = "Instantiating utility class.";

    /** ########## Error Messages ########## */
    static final String INVALID_CONTEXT_INSTANCE = "::Invalid Context instance: ";

    /** ########## Information Messages ########## */
    static final String CONNECTION_CHANGE_UNREGISTER_NOK = "::Unable to unregister receiver.";
    static final String CONNECTION_CHANGE_UNREGISTER_OK = "::Unregistered receiver.";
    static final String CONNECTION_CHANGE_REGISTER_OK = "::Registered receiver.";

    /** Instantiates a new NetworkConstants. Private to prevent instantiation. */
    private NetworkConstants() {
        throw new AssertionError(ASSERTION_ERROR); // Throw an exception if this *is* ever called
    }

}
