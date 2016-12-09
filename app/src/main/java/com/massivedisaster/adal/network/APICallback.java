package com.massivedisaster.adal.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.massivedisaster.adal.BuildConfig;
import com.massivedisaster.adal.R;

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

    public abstract void onSuccess(T t);

    public abstract void onError(APIError e, boolean serverError);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

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

            } catch (IOException e) {
                e.printStackTrace();
            }


            processError(new APIError(mContext.getString(R.string.error_network_general)), true);
            return;
        }

        if (response.body() == null) {
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
