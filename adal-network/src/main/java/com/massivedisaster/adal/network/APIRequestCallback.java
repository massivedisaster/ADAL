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

package com.massivedisaster.adal.network;

import android.content.Context;
import android.util.Log;

import com.massivedisaster.adal.utils.LogUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Base request API callbacks.
 *
 * @param <T> the type of the data in the requests.
 */
public abstract class APIRequestCallback<T extends APIErrorListener> implements Callback<T> {

    private final Context mContext;

    /**
     * Constructcs {@link APIRequestCallback}
     *
     * @param context the context.
     */
    public APIRequestCallback(Context context) {
        mContext = context;
    }

    /**
     * @param t response
     */
    public abstract void onSuccess(T t);

    /**
     * @param error         the error.
     * @param isServerError is a server error.
     */
    public abstract void onError(APIError error, boolean isServerError);

    /**
     * Called when response ends.
     *
     * @param call     the call.
     * @param response the response.
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (call.isCanceled()) {
            return;
        }

        if (response == null) {
            processError(new APIError(mContext.getString(R.string.error_network_general)), true);
            return;
        }

        if (response.errorBody() != null) {
            try {
                Converter<ResponseBody, T> errorConverter = getRetrofitConverter(call);

                T error = errorConverter.convert(response.errorBody());

                if (error != null) {
                    processError(new APIError(error.getErrorCode(), error.getError()), true);
                    return;
                }
            } catch (IllegalAccessException e) {
                LogUtils.logErrorException(APIRequestCallback.class, e);
            } catch (IOException e) {
                LogUtils.logErrorException(APIRequestCallback.class, e);
            } catch (NoSuchFieldException e) {
                LogUtils.logErrorException(APIRequestCallback.class, e);
            }

            processError(new APIError(mContext.getString(R.string.error_network_general)), true);
            return;
        }

        if (response.body() == null && !response.isSuccessful()) {
            processError(new APIError(mContext.getString(R.string.error_network_general)), true);
            return;
        }

        onSuccess(response.body());
    }

    /**
     * Retrive the retrofit converter.
     *
     * @param call the call.
     * @return return the converter.
     * @throws NoSuchFieldException     if a field with the specified name is
     *                                  not found.
     * @throws IllegalAccessException   if object
     *                                  is enforcing Java language access control and the underlying
     *                                  field is inaccessible.
     * @throws IllegalArgumentException if the specified object is not an
     *                                  instance of the class or interface declaring the underlying
     *                                  field (or a subclass or implementor thereof).
     */
    @SuppressWarnings("unchecked")
    private Converter<ResponseBody, T> getRetrofitConverter(Call<T> call) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        Field f = call.getClass().getDeclaredField("delegate");
        f.setAccessible(true);
        Object obj = f.get(call);

        f = obj.getClass().getDeclaredField("serviceMethod");
        f.setAccessible(true);
        obj = f.get(obj);

        f = obj.getClass().getDeclaredField("responseConverter");
        f.setAccessible(true);

        return (Converter<ResponseBody, T>) f.get(obj);
    }

    /**
     * Called on request failure.
     *
     * @param call the call.
     * @param t    the throwable.
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {

        if (call.isCanceled()) {
            return;
        }

        if (t != null) {
            LogUtils.logErrorException(APIRequestCallback.class, t);

            if ((t instanceof UnknownHostException) && t.getMessage() != null) {
                processError(new APIError(mContext.getString(R.string.error_network_no_connection)), true);
                return;
            }
        }

        processError(new APIError(mContext.getString(R.string.error_network_general)), true);
    }

    /**
     * Processes de error.
     *
     * @param error       the error.
     * @param serverError true if its a server error.
     */
    private void processError(APIError error, boolean serverError) {

        if (BuildConfig.DEBUG) {
            Log.e(APIRequestCallback.class.getCanonicalName(), error.getMessage());
        }

        onError(error, serverError);
    }
}
