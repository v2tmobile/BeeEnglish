package com.ahiho.apps.beeenglish.model;

/**
 * Created by theptokim on 11/28/16.
 */

public class ResponseData {
    private boolean responseState;
    private String responseData;

    public ResponseData() {
        this.responseState = false;
        this.responseData = "";
    }
    public ResponseData(boolean responseState, String responseData) {
        this.responseState = responseState;
        this.responseData = responseData;
    }

    public boolean isResponseState() {
        return responseState;
    }

    public void setResponseState(boolean responseState) {
        this.responseState = responseState;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

}
