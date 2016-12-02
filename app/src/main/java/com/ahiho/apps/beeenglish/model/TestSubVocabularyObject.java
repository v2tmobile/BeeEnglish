package com.ahiho.apps.beeenglish.model;

/**
 * Created by theptokim on 11/28/16.
 */

public class TestSubVocabularyObject {
    private  int id;
    private int type;
    private String question;
    private String trueData;
    private String hint;

    public TestSubVocabularyObject(int id,  int type, String question, String trueData, String hint) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.trueData = trueData;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTrueData() {
        return trueData;
    }

    public void setTrueData(String trueData) {
        this.trueData = trueData;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
