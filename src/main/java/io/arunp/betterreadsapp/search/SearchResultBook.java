package io.arunp.betterreadsapp.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResultBook {

    private String key;

    private String title;

    @JsonProperty("author_name")
    private List<String> authorNames;

    @JsonProperty("cover_i")
    private String coverId;

    @JsonProperty("first_publish_year")
    private int firstPublishYear;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }
}
