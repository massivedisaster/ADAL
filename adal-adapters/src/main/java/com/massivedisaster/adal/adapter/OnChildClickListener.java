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

package com.massivedisaster.adal.adapter;

import android.view.View;

/**
 * Listener called when an element is clicked.
 *
 * @param <T> The type of the elements.
 */
public interface OnChildClickListener<T> {
    /**
     * Method called when an element is clicked.
     *
     * @param view     The view clicked.
     * @param t        The element clicked.
     * @param position The position of the element clicked.
     */
    void onChildClick(View view, T t, int position);
}
