package com.gxuwz.visitor.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gxuwz.visitor.model.bean.Question;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParser {
    public static List<Question> parseQuestions(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Question>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
