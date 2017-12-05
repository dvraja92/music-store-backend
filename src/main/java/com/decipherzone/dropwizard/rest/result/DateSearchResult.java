package com.decipherzone.dropwizard.rest.result;

public class DateSearchResult<T> extends SearchResult<T> {
    private long beforeDate;
    private long afterDate;

    public long getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(long beforeDate) {
        this.beforeDate = beforeDate;
    }

    public long getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(long afterDate) {
        this.afterDate = afterDate;
    }
}
