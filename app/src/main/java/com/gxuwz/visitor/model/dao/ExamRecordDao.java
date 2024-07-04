package com.gxuwz.visitor.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.gxuwz.visitor.model.bean.ExamRecord;

import java.util.List;

@Dao
public interface ExamRecordDao {
    @Insert
    void insert(ExamRecord record);

    @Query("SELECT * FROM exam_record")
    List<ExamRecord> getAllRecords();

    @Query("SELECT * FROM exam_record WHERE id = :id")
    ExamRecord getRecordById(int id);

    @Update
    void update(ExamRecord record);

    @Delete
    void delete(ExamRecord record);
    @Query("DELETE FROM exam_record")
    void deleteAll();
}
