package com.an.biometric.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements BiometricCallback {

    private Button button_diem_danh;
    private Button button_dang_ky;
    private Button button_reset;
    private Button button_refresh;
    public TextView textView_Name;
    public TextView textView_Student_id;
    public TextView textView_Class_id;
    public EditText editText_ip;
    BiometricManager mBiometricManager;
    public final static String IP_KEY ="server_ip";
    public final static String CLASS_ID ="class_id";
    private Timer timer = new Timer();
    private TimerTask task_update;
    private  Update_GUI_Dang_Nhap update_gui_dang_nhap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView_Class_id = findViewById(R.id.textView_Class_id);
        this.textView_Name = findViewById(R.id.textView_Name);
        this.textView_Student_id = findViewById(R.id.textView_Student_id);
        this.editText_ip = findViewById(R.id.editTextTextIp);
        this.button_diem_danh = findViewById(R.id.btn_authenticate);
        this.button_dang_ky = findViewById(R.id.btn_register);
        this.button_reset = findViewById(R.id.btn_reset);
        this.button_refresh = findViewById(R.id.btn_refresh);
        this.button_diem_danh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBiometricManager = new BiometricManager.BiometricBuilder(MainActivity.this)
                        .setTitle(getString(R.string.biometric_title))
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .build();

                mBiometricManager.authenticate(MainActivity.this);
            }
        });

        this.button_dang_ky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_gui_dang_nhap.stop();
                String message = editText_ip.getText().toString();
                String class_id = textView_Class_id.getText().toString();

                Intent intent= new Intent(MainActivity.this ,RegisterActitivy.class);

                intent.putExtra(IP_KEY,message);
                intent.putExtra(CLASS_ID,class_id);

                startActivity(intent);

            }
        });
        final Context appcontext = this.getApplicationContext();
        this.button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_info_from_server();
            }
        });
        this.button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File_Utils.writeToFile("",appcontext,File_Utils.KEY_SIGN_DATA);
                String key_sign = File_Utils.readFromFile(appcontext,File_Utils.KEY_SIGN_DATA);
                if (key_sign==null){
                    Toast.makeText(getApplicationContext(), "Vui lòng đăng ký trước khi điểm danh.", Toast.LENGTH_LONG).show();
                    button_diem_danh.setEnabled(false);
                }
            }
        });
        String current_ip = File_Utils.readFromFile(appcontext,"ip.config");
        if (current_ip == null){
            current_ip = "";
        }
        this.editText_ip.setText(current_ip);

        this.editText_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                File_Utils.writeToFile(editText_ip.getText().toString(),appcontext,"ip.config");
            }
        });

        this.update_gui_dang_nhap = new Update_GUI_Dang_Nhap(MainActivity.this);
        //Gọi hàm execute để kích hoạt tiến trình
        this.update_gui_dang_nhap.execute();
    }

    public void load_info_from_server(){
        MainActivity this_class=  this;
        HttpUtils.getByUrl("http://" + this_class.editText_ip.getText() + ":1903/get_current_class",
                new Get_Class_Id_Response_Handler(
                        this_class.getApplicationContext(),
                        this_class.textView_Class_id
                )
        );
        //
        // Read key_sign
        // /
        if (this_class.textView_Class_id==null){
            return;
        }
        String key_sign = File_Utils.read_key_sign(this_class, (String) this_class.textView_Class_id.getText());
        Log.i("Nguyen","Key_sign: "+key_sign);

        JSONObject pr = new JSONObject();
        if (key_sign==null){
            return;
        }

        try {
            pr.put("key_sign",key_sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DiemDanh.callapi_post(
                this_class.getApplicationContext(),
                "http://" + this_class.editText_ip.getText() + ":1903/get_student_info",
                pr,
                new Get_Student_Info_Handler(this_class)
        );
    }
    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        mBiometricManager.cancelAuthentication();
    }


    private void register(String student_id){


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAuthenticationSuccessful() {
        JSONObject pr = new JSONObject();
        String key_sign = File_Utils.read_key_sign(this.getApplicationContext(),(String) this.textView_Class_id.getText());
        if (key_sign==null){
            Toast.makeText(getApplicationContext(), "Vui lòng đăng ký trước khi điểm danh.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i("Nguyen","Key_sign: "+key_sign);
        try {
            pr.put("key_sign",key_sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DiemDanh.callapi_post(
                this.getApplicationContext(),
                "http://" + this.editText_ip.getText() + ":1903/check",
                pr,
                new Diem_danh_Response_Handler(this.getApplicationContext())
        );

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }
}
