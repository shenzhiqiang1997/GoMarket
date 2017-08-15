package com.buhinia.search2.model;

import org.litepal.crud.DataSupport;

/**
 * 浏览历史对应的实体类
 */

public class BrowseHistory extends DataSupport {
    //浏览商品的id
    private long goodId;

    public BrowseHistory(long goodId) {
        this.goodId = goodId;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    //重写equals和hashCode方法用于判定是否重复
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowseHistory that = (BrowseHistory) o;

        return goodId == that.goodId;

    }

    @Override
    public int hashCode() {
        return (int) (goodId ^ (goodId >>> 32));
    }
}
