package com.an.biometric.sample;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class DiemDanh {



    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void callapi_post(Context context, String url, JSONObject param,AsyncHttpResponseHandler handler){

        try{

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            StringEntity entity = null;
            try {
                entity = new StringEntity(param.toString());
                Log.i("Nguyen",param.toString());
                Log.i("Nguyen",url);
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(context, "Lỗi input."+e.toString(), Toast.LENGTH_LONG).show();
            }
            HttpUtils.post(url,context,entity,handler);
        }catch (Exception ex){
            Toast.makeText(context, "Lỗi." +ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
