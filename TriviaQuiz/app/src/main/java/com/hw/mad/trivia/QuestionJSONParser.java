package com.hw.mad.trivia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * QuestionJSONParser.java
 * Homework 03
 * Sanket Patil
 * Atul Kumar Banwar
 */
public class QuestionJSONParser {
    static ArrayList<Question> pasrseQuestions(String in) throws JSONException {
        ArrayList<Question> questionsList = new ArrayList<Question>();

        JSONObject root = new JSONObject(in);

        JSONArray questionArray = root.getJSONArray("questions");

        for (int i = 0; i < questionArray.length(); i++) {
            JSONObject questionArrayJSONObject = questionArray.getJSONObject(i);

            Question question = Question.getPerson(questionArrayJSONObject);

            questionsList.add(question);
        }
        return questionsList;
    }
}
