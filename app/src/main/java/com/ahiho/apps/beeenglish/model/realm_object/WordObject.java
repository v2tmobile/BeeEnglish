package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by theptokim on 12/12/16.
 */

public class WordObject extends RealmObject {
    @Index
    private String word;
    private String description;

    public WordObject() {
    }

    public WordObject(String word, String description) {
        this.word = word;
        this.description = description;
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
}
