package com.dejia.anju.net;

import java.util.Map;

public class WebSignData {
    private String url;
    private Map<String, String> httpHeaders;

    public WebSignData(String url, Map<String, String> httpHeaders) {
        this.url = url;
        this.httpHeaders = httpHeaders;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
