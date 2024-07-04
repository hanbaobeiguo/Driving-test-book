package com.gxuwz.visitor.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.bean.User;

import java.util.List;
@Dao
public interface QuestionDao {
    @Insert
    void insert(Question question);

    @Query("SELECT * FROM question where subject = :subject")
    List<Question> getAllQuestionsBySubject(int subject);

    @Query("SELECT * FROM question WHERE status = 2 ")
    List<Question> getAllErrorQuestions();
    @Query("SELECT * FROM question WHERE status != 0 ")
    List<Question> getReviewQuestions();
    @Query("SELECT * FROM question WHERE id = :id")
    Question getQuestionById(int id);


    @Update
    void update(Question question);
    @Query("UPDATE question SET status = 0 WHERE id = :id")
    void updateStatusById(int id);

    @Query("UPDATE question SET status = 0")
    void updateStatusAll();
    @Delete
    void delete(Question question);
    @Query("DELETE FROM question")
    void deleteAllQuestions();
}
