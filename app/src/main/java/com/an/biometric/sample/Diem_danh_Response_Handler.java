package com.an.biometric.sample;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Diem_danh_Response_Handler extends AsyncHttpResponseHandler {
    private  Context context;
    public Diem_danh_Response_Handler(Context context){
        this.context = context;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String msg = "N/A";
        try {
            JSONObject jsonResponse = new JSONObject(new String(responseBody));
            msg = jsonResponse.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        Toast.makeText(this.context,msg , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        String msg = "N/A";
        switch (statusCode){
            case 0: {
                msg = "Timeout";
                break;
            }
            default: {
                msg = "Error code: " + statusCode;
                break;
            }
        }
        Toast.makeText(this.context,msg , Toast.LENGTH_LONG).show();
    }



}
