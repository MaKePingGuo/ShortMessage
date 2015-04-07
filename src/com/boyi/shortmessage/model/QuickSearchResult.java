package com.boyi.shortmessage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import com.boyi.shortmessage.utils.SearchResultComparator;

public class QuickSearchResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6040658279747404543L;

    public ArrayList<SearchResultItem> SearchResult = new ArrayList<SearchResultItem>();

    public void addResultList(ArrayList<SearchResultItem> list) {
        for (SearchResultItem item :list) {
            if (!SearchResult.contains(item)) {
                SearchResult.add(item);
            }
        }
        sortList();
    }

    public void sortList() {
        SearchResultComparator src = new SearchResultComparator();
        Collections.sort(SearchResult, src);
    }
}
