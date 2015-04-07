package com.boyi.shortmessage.utils;

import java.util.Comparator;

import com.boyi.shortmessage.model.SearchResultItem;

public class SearchResultComparator implements Comparator<SearchResultItem> {

    public SearchResultComparator() {
    }

    @Override
    public int compare(SearchResultItem lhs, SearchResultItem rhs) {
        return rhs.Edit_time.compareTo(lhs.Edit_time);
    }
}
