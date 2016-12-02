package com.ahiho.apps.beeenglish.model;

import java.util.List;

/**
 * Created by theptokim on 11/28/16.
 */

public class TestVocabularyObject {
    private int id;
    private String title;
    List<TestSubVocabularyObject> data;

    public TestVocabularyObject(int id, String title, List<TestSubVocabularyObject> data) {
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TestSubVocabularyObject> getData() {
        return data;
    }

    public void setData(List<TestSubVocabularyObject> data) {
        this.data = data;
    }
}
