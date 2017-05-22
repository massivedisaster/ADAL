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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.massivedisaster.adal.sample.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    public static IRequests getAccountAdapter() {
        return getRetrofit().create(IRequests.class);
    }

    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().create();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient(BuildConfig.API_TIMEOUT))
                .build();
    }

    private static OkHttpClient getOkHttpClient(long timeout) {
        return new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }
}
