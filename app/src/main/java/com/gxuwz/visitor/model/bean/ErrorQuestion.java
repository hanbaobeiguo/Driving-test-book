package com.gxuwz.visitor.model.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "error_question")
public class ErrorQuestion {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String explains;
    private String url;
    private Integer userId;
    private String UserAnswer;

    public ErrorQuestion() {
    }

    public ErrorQuestion(String userAnswer, String question, String answer, String item1, String item2, String item3, String item4, String explains, String url, Integer userId) {
        this.UserAnswer = userAnswer;
        this.question = question;
        this.answer = answer;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.explains = explains;
        this.url = url;
        this.userId = userId;
    }

    public ErrorQuestion(Question question,Integer userId) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.answer = question.getAnswer();
        this.item1 = question.getItem1();
        this.item2 = question.getItem2();
        this.item3 = question.getItem3();
        this.item4 = question.getItem4();
        this.explains = question.getExplains();
        this.url = question.getUrl();
        this.userId = userId;;
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        UserAnswer = userAnswer;
    }

    @Override
    public String toString() {
        return "ErrorQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", item1='" + item1 + '\'' +
                ", item2='" + item2 + '\'' +
                ", item3='" + item3 + '\'' +
                ", item4='" + item4 + '\'' +
                ", explains='" + explains + '\'' +
                ", url='" + url + '\'' +
                ", userId=" + userId +
                ", UserAnswer='" + UserAnswer + '\'' +
                '}';
    }


}