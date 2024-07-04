package com.gxuwz.visitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.model.bean.User;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查用户登录状态
        MyApplication myApplication = (MyApplication) getApplication();
        User currentUser = myApplication.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            // 跳转到登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // 关闭当前活动
        }
    }
}
