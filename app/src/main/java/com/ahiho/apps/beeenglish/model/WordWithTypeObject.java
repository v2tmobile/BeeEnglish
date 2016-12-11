package com.ahiho.apps.beeenglish.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 11/28/16.
 */

public class WordWithTypeObject extends RealmObject {
    @PrimaryKey
    private long id;
    private int type;
    @Index
    private String word;
    private String description;

    public WordWithTypeObject() {
    }

    public WordWithTypeObject(long id, WordObject wordObject, int type) {
        this.id=id;
        this.word = wordObject.getWord();
        this.description = wordObject.getDescription();
        this.type=type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
