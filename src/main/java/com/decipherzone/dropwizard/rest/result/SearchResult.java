package com.decipherzone.dropwizard.rest.result;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {
    private List<T> values = new ArrayList<>();
    private int skip;
    private int limit;
    private Long total;

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
