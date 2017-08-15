package com.buhinia.search2.model;

import org.litepal.crud.DataSupport;

/**
 * 活动对应的实体类
 */

public class ActivityListItem extends DataSupport {
    //活动的网址
    private String webUrl;

    public ActivityListItem(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
