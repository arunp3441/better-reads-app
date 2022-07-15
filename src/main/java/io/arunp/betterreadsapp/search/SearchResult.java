package io.arunp.betterreadsapp.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResult {

    private int numFound;

    @JsonProperty("docs")
    private List<SearchResultBook> searchResultBooks;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public List<SearchResultBook> getSearchResultBooks() {
        return searchResultBooks;
    }

    public void setSearchResultBooks(List<SearchResultBook> searchResultBooks) {
        this.searchResultBooks = searchResultBooks;
    }
}
