package com.gxuwz.visitor.util;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.gxuwz.visitor.model.bean.ErrorQuestion;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.model.dao.UserDao;

@Database(entities = {User.class, ErrorQuestion.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "mydatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
