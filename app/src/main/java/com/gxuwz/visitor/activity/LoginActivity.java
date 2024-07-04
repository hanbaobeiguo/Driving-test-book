package com.gxuwz.visitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.api.LoginService;
import com.gxuwz.visitor.model.bean.ExamRecord;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.model.vo.LoginResponse;
import com.gxuwz.visitor.network.RetrofitClient;
import com.gxuwz.visitor.util.AppDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etPhone, etPassword;
    private ImageView btn_register, btn_forget_password;
    private CheckBox cbAccept;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = MyApplication.database;
        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_forget_password = findViewById(R.id.btn_forget_password);
        cbAccept = findViewById(R.id.cb_accept);

        btnLogin.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (cbAccept.isChecked()) {
                    login();
                } else {
                    Toast.makeText(getApplicationContext(), "未同意协议", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget_password:
                Intent intent1 = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private void login() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "输入框不能为空", Toast.LENGTH_SHORT).show();
        } else {
            MyApplication.appExecutors.diskIO().execute(() -> {
                User user = db.userDao().getUserByPhone(phone);
                if (user != null) {
                    if (user.getPwd().equals(password)) {
                        saveUserInfoAndNavigate(user, phone);
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
                    });
                }
            });

//            httpMethod(phone, password);
        }
    }

    private void httpMethod(String phone, String password) {
        Retrofit retrofit = RetrofitClient.getClient("http://10.0.2.2:4523/m1/4712747-4364972-default/");
        LoginService apiService = retrofit.create(LoginService.class);

        Call<LoginResponse> loginCall = apiService.Login(phone, password);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode().equals("200")) {
                        User user = response.body().getData();
                        saveUserInfoAndNavigate(user, phone);
                    } else {
                        Toast.makeText(getApplicationContext(), "消息提示: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginActivity", "Response not successful: " + response.message());
                    Toast.makeText(getApplicationContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "请求失败", t);
                Toast.makeText(getApplicationContext(), "登录失败，请重试", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveUserInfoAndNavigate(User user, String phone) {
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setCurrentUser(user);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("LoginUserPhone", phone);
        startActivity(intent);
        finish();
    }
}
