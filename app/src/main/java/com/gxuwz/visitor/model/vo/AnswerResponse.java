package com.gxuwz.visitor.model.vo;

import com.gxuwz.visitor.model.bean.Question;

import java.util.List;
import java.util.Map;

public class AnswerResponse {
    private String reason;
    private Map<String, String> result;

    // Getters and setters
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Map<String, String> getResult() { return result; }
    public void setResult(Map<String, String> result) { this.result = result; }
}
