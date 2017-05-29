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

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Log utilities.
 */
public final class LogUtils {

    /**
     * Private constructor to avoid user implement as a single instance instead of a Singleton
     */
    private LogUtils() {

    }

    /**
     * Logs a error exception
     *
     * @param enclosingClass the caller class
     * @param exception      the exception
     */
    public static void logErrorException(@NonNull Class<?> enclosingClass, @NonNull Exception exception) {
        String message = exception.getMessage() == null ? "Exception error." : exception.getMessage();
        Log.e(enclosingClass.getName(), message, exception);
    }

}
