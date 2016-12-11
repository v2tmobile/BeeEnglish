package com.ahiho.apps.beeenglish.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 11/28/16.
 */

public class CategoryObject extends RealmObject {
    @PrimaryKey
    private int id;
    private int count;

    public CategoryObject() {
    }

    public CategoryObject(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
