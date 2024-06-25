package com.gxuwz.visitor.model.vo;

import com.gxuwz.visitor.model.bean.Question;

import java.util.List;

public class QuestionResponse {
    private String reason;
    private List<Question> result;

    // Getters and setters
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public List<Question> getResult() { return result; }
    public void setResult(List<Question> result) { this.result = result; }
}
