package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class CommunicationSubSecondObject extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String icon;
    private RealmList<CommunicationSubSecondDetailObject> data;

    public CommunicationSubSecondObject() {
    }

    public CommunicationSubSecondObject(int id, String name, String description, String icon, RealmList<CommunicationSubSecondDetailObject> data) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public RealmList<CommunicationSubSecondDetailObject> getData() {
        return data;
    }

    public void setData(RealmList<CommunicationSubSecondDetailObject> data) {
        this.data = data;
    }
}
