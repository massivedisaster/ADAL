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

package com.massivedisaster.adal.fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public abstract class AbstractRequestFragment extends AbstractBaseFragment {

    private List<Call<?>> mLstCallbacks = new ArrayList<>();

    public <U> Call<U> addRequest(Call<U> call) {
        mLstCallbacks.add(call);

        return call;
    }

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
