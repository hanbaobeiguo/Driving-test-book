package com.gxuwz.visitor.model.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gxuwz.visitor.model.bean.ErrorQuestion;

import java.util.List;

public interface ErrorQuestionDao {
    @Insert
    void insert(ErrorQuestion errorQuestion);

    @Query("SELECT * FROM error_question")
    List<ErrorQuestion> getAllErrorQuestion();

    @Query("SELECT * FROM error_question WHERE id = :Id")
    ErrorQuestion getErrorQuestionById(int Id);

    @Update
    void update(ErrorQuestion errorQuestion);

    @Delete
    void delete(ErrorQuestion errorQuestion);
}
