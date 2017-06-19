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

package com.massivedisaster.adal.fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Abstract class for fragments that do requests.
 */
public abstract class AbstractRequestFragment extends AbstractBaseFragment {

    private final List<Call<?>> mLstCallbacks = new ArrayList<>();

    /**
     * Constructs {@link AbstractRequestFragment}
     *
     * @param call the call.
     * @param <U>  the call type.
     * @return the call.
     */
    public <U> Call<U> addRequest(Call<U> call) {
        mLstCallbacks.add(call);

        return call;
    }

    /**
     * Cancel all pending requests.
     */
    public void cancelAllRequests() {
        for (Call<?> c : mLstCallbacks) {
            c.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelAllRequests();
    }
}
