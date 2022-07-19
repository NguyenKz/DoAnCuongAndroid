package com.an.biometric.sample;

import static java.lang.Thread.sleep;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

public class Update_GUI_Dang_Nhap extends AsyncTask<Void, Integer, Void> {

    MainActivity contextParent;
    private String class_id = "";
    private String server_ip = "";
    public  boolean is_run = true;
    public Update_GUI_Dang_Nhap(MainActivity contextParent) {
        this.contextParent = contextParent;
        this.is_run = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
        Toast.makeText(contextParent, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(this.is_run){
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            publishProgress(0);

        }
        return null;

    }

    public  void stop(){
        this.is_run = false;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                contextParent.load_info_from_server();
            }
        }, 0);


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
    }
}
