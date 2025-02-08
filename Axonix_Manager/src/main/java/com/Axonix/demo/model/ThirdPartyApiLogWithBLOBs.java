package com.Axonix.demo.model;

public class ThirdPartyApiLogWithBLOBs extends ThirdPartyApiLog {
    private String requestData;

    private String responseData;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData == null ? null : requestData.trim();
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData == null ? null : responseData.trim();
    }
}