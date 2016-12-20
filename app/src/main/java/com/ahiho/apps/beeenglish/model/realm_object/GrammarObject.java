package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class GrammarObject extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<SubObject> data;

    public GrammarObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<SubObject> getData() {
        return data;
    }

    public void setData(RealmList<SubObject> data) {
        this.data = data;
    }
}
