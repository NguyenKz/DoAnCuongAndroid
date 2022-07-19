package com.an.biometric.sample;

import static com.an.biometric.sample.MainActivity.IP_KEY;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActitivy extends AppCompatActivity implements BiometricCallback {

    private Button button_diem_danh;
    private Button button_dang_ky;
    private EditText editText_ip;
    private EditText editText_student_id;
    private EditText editText_student_name;
    BiometricManager mBiometricManager;
    private String class_id = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bundle extras = getIntent().getExtras();
        String server_ip = extras.getString(MainActivity.IP_KEY);
        this.class_id = extras.getString(MainActivity.CLASS_ID);
        this.editText_ip = findViewById(R.id.editTextTextIp);
        this.editText_ip.setText(server_ip);
        this.editText_student_id = findViewById(R.id.editTextTextStudentId);
        this.editText_student_name = findViewById(R.id.editTextTextStudentName);
        this.button_diem_danh = findViewById(R.id.btn_authenticate);
        this.button_dang_ky = findViewById(R.id.btn_register);
        this.button_dang_ky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBiometricManager = new BiometricManager.BiometricBuilder(RegisterActitivy.this)
                        .setTitle(getString(R.string.biometric_title))
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .build();

                mBiometricManager.authenticate(RegisterActitivy.this);
            }
        });

        this.button_diem_danh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String message = editText_ip.getText().toString();

                    Intent intent= new Intent(RegisterActitivy.this ,MainActivity.class);

                    intent.putExtra(IP_KEY,message);

                    startActivity(intent);
            }
        });

        //
        // Đã có key rồi thì không đăng ký được
        // /

        String key_sign = File_Utils.read_key_sign(this.getApplicationContext(),this.class_id);
        if (key_sign!=null){
            this.button_dang_ky.setEnabled(false);
        }


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
        try {
            pr.put("student_id",this.editText_student_id.getText());
            pr.put("student_name",this.editText_student_name.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DiemDanh.callapi_post(
                this.getApplicationContext(),
                "http://" + this.editText_ip.getText() + ":1903/register",
                pr,
                new Dang_Ky_Response_Handler(this.getApplicationContext())
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
