package com.massivedisaster.adal.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class APICallback<T extends APIErrorListener> implements Callback<T> {

    private Context mContext;

    public APICallback(Context context) {
        mContext = context;
    }

    /**
     * @param t response
     */
    public abstract void onSuccess(T t);

    /**
     * @param error
     * @param isServerError
     */
    public abstract void onError(APIError error, boolean isServerError);

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

            Gson gson = new Gson();
            try {

                T error = gson.fromJson(response.errorBody().string(), ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0]);

                if (error != null) {
                    processError(new APIError(error.getError()), true);
                    return;
                }

            } catch (ClassCastException | IOException e) {
                e.printStackTrace();
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

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        if (call.isCanceled()) {
            return;
        }

        if (t != null) {
            t.printStackTrace();

            if ((t instanceof UnknownHostException) && t.getMessage() != null) {
                processError(new APIError(mContext.getString(R.string.error_network_no_connection)), true);
                return;
            }
        }

        processError(new APIError(mContext.getString(R.string.error_network_general)), true);
    }

    private void processError(APIError error, boolean serverError) {

        if (BuildConfig.DEBUG) {
            Log.e(APICallback.class.getCanonicalName(), error.getMessage());
        }

        onError(error, serverError);
    }
}
