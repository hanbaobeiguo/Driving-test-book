package com.gxuwz.visitor.util;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;

import com.gxuwz.visitor.model.bean.ErrorQuestion;
import com.gxuwz.visitor.model.bean.ExamRecord;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.model.dao.ErrorQuestionDao;
import com.gxuwz.visitor.model.dao.ExamRecordDao;
import com.gxuwz.visitor.model.dao.QuestionDao;
import com.gxuwz.visitor.model.dao.UserDao;

@Database(entities = {ExamRecord.class,User.class, ErrorQuestion.class, Question.class}, version =4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract QuestionDao questionDao();
    public abstract ErrorQuestionDao errorQuestionDao();
    public abstract ExamRecordDao examRecordDao();

    private static volatile AppDatabase INSTANCE;

    public static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE error_question ADD COLUMN UserAnswer TEXT ");
        }
    };
    public static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE question ADD COLUMN subject INTEGER NOT NULL DEFAULT 0");
        }
    };
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 修改 score 列的类型
            database.execSQL("ALTER TABLE exam_record RENAME TO exam_record_old");
            database.execSQL("CREATE TABLE IF NOT EXISTS exam_record (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, time TEXT, score INTEGER, user_id INTEGER NOT NULL)");
            database.execSQL("INSERT INTO exam_record (id, time, score, user_id) SELECT id, time, CAST(score AS INTEGER), user_id FROM exam_record_old");
            database.execSQL("DROP TABLE exam_record_old");
        }
    };



    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "exam1.db")
                            .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
