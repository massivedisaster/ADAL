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
