package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class CommunicationSubSecondDetailObject extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String description;

    public CommunicationSubSecondDetailObject() {
    }

    public CommunicationSubSecondDetailObject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
