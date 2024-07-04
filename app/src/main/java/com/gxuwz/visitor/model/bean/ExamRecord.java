package com.gxuwz.visitor.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName="exam_record")
public class ExamRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "time")
    private  String Time;
    @ColumnInfo(name = "score")
    private Integer score;
    @ColumnInfo(name = "user_id")
    private int userId;

    public ExamRecord() {
    }

    public ExamRecord( String time, Integer score, int userId) {

        this.Time = time;
        this.score = score;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ExamRecord{" +
                "id=" + id +
                ", Time='" + Time + '\'' +
                ", score=" + score +
                ", userId=" + userId +
                '}';
    }
}
