package com.ahiho.apps.beeenglish.model.realm_object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 12/12/16.
 */

public class SubDetailObject extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String detail;
    private String link;
    private long testId;

    public SubDetailObject() {
    }

    public SubDetailObject(int id, String name, String detail, String link, long testId) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.link = link;
        this.testId=testId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }
}
