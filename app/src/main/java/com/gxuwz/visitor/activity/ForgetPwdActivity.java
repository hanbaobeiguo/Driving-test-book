package com.gxuwz.visitor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.util.AppDatabase;

public class ForgetPwdActivity extends Activity {

    private AppDatabase db;
    private EditText et_phone,et_captcha,et_password;
    private Button btn_submit,btn_captcha;
    private LinearLayout loginLayout;
    private String captcha;
    private static final int REQUEST_CODE_RECEIVE_SMS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
//        动态请求权限
        checkPermissions();
        db =  MyApplication.database;

        initView();
        clickMethod();

    }

    private void clickMethod() {

        // 点击发送验证码按钮
        btn_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = et_phone.getText().toString();
                captcha = generateVerificationCode();
                sendSMS(phoneNumber, "你正在修改密码 您的验证码为: " + captcha);
            }
        });

        // 点击注册按钮
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone.getText().toString().isEmpty() || et_password.getText().toString().isEmpty()  || et_captcha.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "有未填项，请重试", Toast.LENGTH_SHORT).show();
                }
                else if (et_phone.getText().toString().length() != 11){
                    Toast.makeText(getApplicationContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
                }
                else if (!captcha.equals(et_captcha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                }
                else{
                    register();
                }
            }
        });

        // 返回登录页面
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLoginLayout();
            }
        });


    }



    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);

        et_captcha = findViewById(R.id.et_captcha);

        btn_captcha = findViewById(R.id.btn_captcha);
        btn_submit = findViewById(R.id.btn_submit);


        loginLayout = findViewById(R.id.login_layout);
    }

    // 发送短信
    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }


    
    private String generateVerificationCode() {
        // 生成一个随机的验证码
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }



//    动态请求权限方法
    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_RECEIVE_SMS);
        }
    }
    //
    private void register() {
        String phoneNumber = et_phone.getText().toString();
        String password = et_password.getText().toString();

        MyApplication.appExecutors.diskIO().execute(() -> {
            User user = db.userDao().getUserByPhone(phoneNumber);

            if (user != null) {
                user.setPwd(password);
                db.userDao().update(user);
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "修改密码成功，请返回登录", Toast.LENGTH_SHORT).show();
                    loadLoginLayout();
                });

            } else {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "该手机号未注册，请先注册", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }




    // 跳转到登录页面
    private void loadLoginLayout() {
        Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}