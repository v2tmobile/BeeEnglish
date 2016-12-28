package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class CommunicationObject extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String icon;
    private RealmList<CommunicationSubObject> data;

    public CommunicationObject() {
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

    public RealmList<CommunicationSubObject> getData() {
        return data;
    }

    public void setData(RealmList<CommunicationSubObject> data) {
        this.data = data;
    }
}
