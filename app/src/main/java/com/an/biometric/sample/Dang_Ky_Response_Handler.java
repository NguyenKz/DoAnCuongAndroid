package com.an.biometric.sample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Dang_Ky_Response_Handler extends AsyncHttpResponseHandler {
    private  Context context;
    public Dang_Ky_Response_Handler(Context context){
        this.context = context;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String msg = "N/A";
        Log.i("Nguyen","Reigster");
        try {
            JSONObject jsonResponse = new JSONObject(new String(responseBody));
            Log.i("Nguyen",jsonResponse.toString());
            msg = jsonResponse.getString("msg");
            String key_sign = jsonResponse.getString("key_sign");
            String class_id = jsonResponse.getString("class_id");
            boolean is_register = jsonResponse.getBoolean("register");

            if (is_register){
                String old_info = File_Utils.readFromFile(this.context,File_Utils.KEY_SIGN_DATA);
                JSONObject object = new JSONObject();
                if (old_info!=null){
                    object = new JSONObject(old_info);
                }
                JSONObject object_level_2 = new JSONObject();
                object_level_2.put("key_sign",key_sign);
                object.put(class_id,object_level_2);

                String data_save = object.toString();
                Log.i("Nguyen","Data: "+data_save);
                File_Utils.writeToFile(data_save,this.context,File_Utils.KEY_SIGN_DATA);
                String haha = File_Utils.readFromFile(this.context,File_Utils.KEY_SIGN_DATA);
                Log.i("Nguyen","Load data: "+haha);
            }else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        Toast.makeText(this.context,msg , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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

}
