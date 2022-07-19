package com.an.biometric.sample;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;

public class HttpUtils {


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, Context context, HttpEntity entiny, AsyncHttpResponseHandler responseHandler) {
        client.post(context, url,entiny,"application/json", responseHandler);
    }

    public static void getByUrl(String url, AsyncHttpResponseHandler responseHandler) {
        Log.i("Nguyen",url);
        client.get(url, responseHandler);
    }
    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return relativeUrl;
    }
}
