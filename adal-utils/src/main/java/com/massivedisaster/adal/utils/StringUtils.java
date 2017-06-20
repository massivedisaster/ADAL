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

package com.massivedisaster.adal.utils;

import java.text.Normalizer;

/**
 * Utils to help to manipulate strings
 */
public final class StringUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private StringUtils() {

    }

    /**
     * Replace all special characters from the string
     *
     * @param string the original string
     * @return the new string
     */
    public static String normalizeString(String string) {
        String stringNormalized = Normalizer.normalize(string, Normalizer.Form.NFD);
        stringNormalized = stringNormalized.replaceAll("[^\\p{ASCII}]", "");
        return stringNormalized;
    }

    /**
     * Remove white spaces from the string
     *
     * @param string the original string
     * @return the new string
     */
    public static String removeWhiteSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }
}
