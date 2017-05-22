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

package com.massivedisaster.adal.sample.network;

import com.massivedisaster.adal.network.APICallback;
import com.massivedisaster.adal.sample.model.Post;

import retrofit2.Call;

public class APIRequests {

    private static IRequests getAdapter() {
        return RetrofitAdapter.getAccountAdapter();
    }

    public static Call getPosts(APICallback<ResponseList<Post>> callObject) {

        Call<ResponseList<Post>> call = getAdapter().getPosts();
        call.enqueue(callObject);

        return call;
    }
}
