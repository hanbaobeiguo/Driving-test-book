package com.gxuwz.visitor;

import android.app.Application;

import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.util.AppDatabase;
import com.gxuwz.visitor.util.AppExecutors;

public class MyApplication extends Application {
    public static AppDatabase database;
    public static AppExecutors appExecutors;
    private User currentUser;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        database = AppDatabase.getDatabase(this);
        appExecutors = new AppExecutors();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
//    删除用户信息
    public void logout() {
    currentUser = null;}
}
