package com.an.biometric.sample;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Get_Class_Id_Response_Handler extends AsyncHttpResponseHandler {
    private  Context context;
    private TextView textView_class_id;
    public Get_Class_Id_Response_Handler(Context context,TextView textView_class_id){
        this.context = context;
        this.textView_class_id = textView_class_id;

    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Log.i("Nguyen","Hhahahaha");
        try {
            JSONObject jsonResponse = new JSONObject(new String(responseBody));
            String class_id = jsonResponse.getString("class_id");
            if (class_id == null){
                class_id = "null";
            }
            if (this.textView_class_id !=null) {
                this.textView_class_id.setText(class_id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Nguyen","Error: "+e.getMessage());
        }
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }

}
