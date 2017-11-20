package com.hw.mad.trivia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Question.java
 * Homework 03
 * Sanket Patil
 * Atul Kumar Banwar
 */

public class Question implements Serializable {
    private int id;
    private String text;
    private String url;
    private ArrayList<String> choices;
    private int answer;

    public Question() {

    }

    public Question(int id, String text, String url, ArrayList<String> choices, int answer) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.choices = choices;
        this.answer = answer;
    }

    public static Question getPerson(JSONObject obj) throws JSONException {
        Question person = new Question();
        person.setId(obj.getInt("id"));
        person.setText(obj.getString("text"));
        if (!obj.isNull("image")) {
            person.setUrl(obj.getString("image"));
        }
        JSONObject choicesObj = obj.getJSONObject("choices");
        JSONArray choiceArray = choicesObj.getJSONArray("choice");
        ArrayList<String> choices = new ArrayList<String>();
        for (int i = 0; i < choiceArray.length(); i++) {
            choices.add(choiceArray.getString(i));
        }
        person.setChoices(choices);
        person.setAnswer(choicesObj.getInt("answer"));
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", choices=" + choices +
                ", answer=" + answer +
                '}';
    }

}
