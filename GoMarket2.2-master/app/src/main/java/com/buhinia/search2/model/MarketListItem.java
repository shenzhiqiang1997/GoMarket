package com.buhinia.search2.model;


import org.litepal.crud.DataSupport;

import java.util.Comparator;


/**
 * 超市对应的实体类
 */
/*此为超市在搜索超市的界面中展示的子项的数据*/
public class MarketListItem extends DataSupport{
    //超市id
    private long marketId;
    //超市名称
    private String marketName;
    //超市地址
    private String marketLocation;
    //超市电话
    private long marketPhoneNumber;
    //超市图片地址
    private String marketImageUrl;
    //超市描述
    private String marketDescription;
    //超市折扣
    private double marketDiscount;
    //超市纬度
    private double marketLatitude;
    //超市经度
    private double marketLongitude;
    //超市点击量
    private long marketClickTimes;
    //超市距离
    private double marketDistance;

    public MarketListItem(long marketId, String marketName, String marketLocation, long marketPhoneNumber, String marketImageUrl, String marketDescription, double marketDiscount, double marketLatitude, double marketLongitude, long marketClickTimes) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.marketLocation = marketLocation;
        this.marketPhoneNumber = marketPhoneNumber;
        this.marketImageUrl = marketImageUrl;
        this.marketDescription = marketDescription;
        this.marketDiscount = marketDiscount;
        this.marketLatitude = marketLatitude;
        this.marketLongitude = marketLongitude;
        this.marketClickTimes = marketClickTimes;
    }

    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketLocation() {
        return marketLocation;
    }

    public void setMarketLocation(String marketLocation) {
        this.marketLocation = marketLocation;
    }

    public long getMarketPhoneNumber() {
        return marketPhoneNumber;
    }

    public void setMarketPhoneNumber(long marketPhoneNumber) {
        this.marketPhoneNumber = marketPhoneNumber;
    }

    public String getMarketImageUrl() {
        return marketImageUrl;
    }

    public void setMarketImageUrl(String marketImageUrl) {
        this.marketImageUrl = marketImageUrl;
    }

    public String getMarketDescription() {
        return marketDescription;
    }

    public void setMarketDescription(String marketDescription) {
        this.marketDescription = marketDescription;
    }

    public double getMarketDiscount() {
        return marketDiscount;
    }

    public void setMarketDiscount(double marketDiscount) {
        this.marketDiscount = marketDiscount;
    }

    public double getMarketLatitude() {
        return marketLatitude;
    }

    public void setMarketLatitude(double marketLatitude) {
        this.marketLatitude = marketLatitude;
    }

    public double getMarketLongitude() {
        return marketLongitude;
    }

    public void setMarketLongitude(double marketLongitude) {
        this.marketLongitude = marketLongitude;
    }

    public long getMarketClickTimes() {
        return marketClickTimes;
    }

    public void setMarketClickTimes(long marketClickTimes) {
        this.marketClickTimes = marketClickTimes;
    }

    public double getMarketDistance() {
        return marketDistance;
    }

    public void setMarketDistance(double marketDistance) {
        this.marketDistance = marketDistance;
    }

    /**
     * 比较器用于超市排序
     */

    //距离比较器
    public static class comparatorWithDistance implements Comparator<MarketListItem> {

        @Override
        public int compare(MarketListItem marketListItem, MarketListItem t1) {
            double d1=marketListItem.getMarketDistance();
            double d2=t1.getMarketDistance();
            if (d1>d2){
                return 1;
            }else if(d1==d2){
                return 0;
            }else{
                return -1;
            }
        }
    }

    //折扣比较器
    public static class comparatorWithDiscount implements Comparator<MarketListItem>{

        @Override
        public int compare(MarketListItem marketListItem, MarketListItem t1) {
            double p1=marketListItem.getMarketDiscount();
            double p2=t1.getMarketDiscount();
            if (p1>p2){
                return 1;
            }else if(p1==p2){
                return 0;
            }else{
                return -1;
            }
        }
    }

    //点击量比较器
    public static class comparatorWithClickTimes implements Comparator<MarketListItem>{

        @Override
        public int compare(MarketListItem marketListItem, MarketListItem t1) {
            double c1=marketListItem.getMarketClickTimes();
            double c2=t1.getMarketClickTimes();
            if (c1>c2){
                return 1;
            }else if (c1==c2){
                return 0;
            }else{
                return -1;
            }
        }
    }
}
