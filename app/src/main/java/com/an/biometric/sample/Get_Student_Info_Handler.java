package com.an.biometric.sample;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Get_Student_Info_Handler extends AsyncHttpResponseHandler {
    private  MainActivity mainActivity;
    public Get_Student_Info_Handler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(new String(responseBody));
            Log.i("Nguyen","Response: "+jsonResponse.toString());
            String student_name = jsonResponse.getString("student_name");
            String student_id = jsonResponse.getString("student_id");

            if (mainActivity.textView_Student_id !=null) {
                Log.i("Nguyen","Set student id: "+student_id);
                mainActivity.textView_Student_id.setText(student_id);
            }
            if (mainActivity.textView_Name !=null) {
                mainActivity.textView_Name.setText(student_name);
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
