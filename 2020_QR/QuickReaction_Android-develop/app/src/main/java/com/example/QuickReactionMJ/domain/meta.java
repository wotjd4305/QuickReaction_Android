package com.example.QuickReactionMJ.domain;



public class meta {

    private long totalCount;
    private long page;
    private long count;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public meta(long totalCount, long page, long count) {
        this.totalCount = totalCount;
        this.page = page;
        this.count = count;
    }

    @Override
    public String toString() {
        return "meta{" +
                "totalCount=" + totalCount +
                ", page=" + page +
                ", count=" + count +
                '}';
    }
}
