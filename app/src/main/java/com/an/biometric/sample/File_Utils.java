package com.an.biometric.sample;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class File_Utils {
    public  static  final String KEY_SIGN_DATA = "key_sign.txt";

    public static String read_key_sign( Context context,String class_id){
        Log.i("Nguyen","Class_id: "+class_id);
        String data = readFromFile(context,KEY_SIGN_DATA);
        if (data==null){
            return null;
        }
        try {
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject class_info = jsonRoot.getJSONObject(class_id);
            return class_info.getString("key_sign");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void writeToFile(String data, Context context,String file_name) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file_name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context,String file_name) {

        String ret = null;
        try {
            File f = new File(file_name);

            InputStream inputStream = context.openFileInput(file_name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString().replace("\n","");
                if (ret.length()<=2){
                    ret = null;
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
