package com.uom.las3014.api.response;

public class TopStoryResponse {
    private String title;
    private String url;

    public TopStoryResponse(final String title, final String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
