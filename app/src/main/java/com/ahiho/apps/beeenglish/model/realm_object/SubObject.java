package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class SubObject extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<SubDetailObject> data;

    public SubObject() {
    }

    public SubObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubObject(int id, String name, RealmList<SubDetailObject> data) {
        this.id = id;
        this.name = name;
        this.data = data;
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

    public RealmList<SubDetailObject> getData() {
        return data;
    }

    public void setData(RealmList<SubDetailObject> data) {
        this.data = data;
    }
}
