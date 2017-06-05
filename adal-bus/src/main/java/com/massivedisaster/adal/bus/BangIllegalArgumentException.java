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

package com.massivedisaster.adal.bus;

/**
 * Illegal argument exception.
 */
public final class BangIllegalArgumentException extends IllegalArgumentException {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION =
            "You can't use primitive type as parameter on subscribed methods when there's no action defined";

    /**
     * Constructs the {@link BangIllegalArgumentException}
     */
    public BangIllegalArgumentException() {
        super(ILLEGAL_ARGUMENT_EXCEPTION);
    }
}
