package com.buhinia.search2.model;

import org.litepal.crud.DataSupport;

import java.util.Comparator;

/**
 * 商品对应的实体类
 */
public class GoodListItem extends DataSupport {

    //商品id(主键)
    private long goodId;
    //所在超市id(抽象外键)
    private long marketId;
    //商品名称
    private String goodName;
    //商品类型
    private String goodType;
    //商品原价
    private double goodOriginalPrice;
    //商品现价
    private double goodCurrentPrice;
    //商品图片地址
    private String goodImageUrl;
    //商品描述
    private String goodDescription;
    //商品点击量
    private long goodClickTimes;
    //商品所在位置与用户的距离
    private  double goodDistance;
    //商品所在超市的位置
    private  String goodSite;

    public GoodListItem(long goodId, long marketId, String goodName, String goodType, double goodOriginalPrice, double goodCurrentPrice, String goodImageUrl, String goodDescription, long goodClickTimes) {
        this.goodId = goodId;
        this.marketId = marketId;
        this.goodName = goodName;
        this.goodType = goodType;
        this.goodOriginalPrice = goodOriginalPrice;
        this.goodCurrentPrice = goodCurrentPrice;
        this.goodImageUrl = goodImageUrl;
        this.goodDescription = goodDescription;
        this.goodClickTimes = goodClickTimes;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public double getGoodOriginalPrice() {
        return goodOriginalPrice;
    }

    public void setGoodOriginalPrice(double goodOriginalPrice) {
        this.goodOriginalPrice = goodOriginalPrice;
    }

    public double getGoodCurrentPrice() {
        return goodCurrentPrice;
    }

    public void setGoodCurrentPrice(double goodCurrentPrice) {
        this.goodCurrentPrice = goodCurrentPrice;
    }

    public String getGoodImageUrl() {
        return goodImageUrl;
    }

    public void setGoodImageUrl(String goodImageUrl) {
        this.goodImageUrl = goodImageUrl;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public void setGoodDescription(String goodDescription) {
        this.goodDescription = goodDescription;
    }

    public String getGoodSite() {
        return goodSite;
    }

    public void setGoodSite(String goodSite) {
        this.goodSite = goodSite;
    }

    public long getGoodClickTimes() {
        return goodClickTimes;
    }

    public void setGoodClickTimes(long goodClickTimes) {
        this.goodClickTimes = goodClickTimes;
    }

    public double getGoodDistance() {
        return goodDistance;
    }

    public void setGoodDistance(double goodDistance) {
        this.goodDistance = goodDistance;
    }


    /**
     * 比较器用于商品排序
     */

    //距离比较器
    public static class comparatorWithDistance implements Comparator<GoodListItem>{

        @Override
        public int compare(GoodListItem goodListItem, GoodListItem t1) {
            double d1=goodListItem.getGoodDistance();
            double d2=t1.getGoodDistance();
            if (d1>d2){
                return 1;
            }else if(d1==d2){
                return 0;
            }else{
                return -1;
            }
        }
    }

    //价格比较器
    public static class comparatorWithCurrentPrice implements Comparator<GoodListItem>{

        @Override
        public int compare(GoodListItem goodListItem, GoodListItem t1) {
            double p1=goodListItem.getGoodCurrentPrice();
            double p2=t1.getGoodCurrentPrice();
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
    public static class comparatorWithClickTimes implements Comparator<GoodListItem>{

        @Override
        public int compare(GoodListItem goodListItem, GoodListItem t1) {
            double c1=goodListItem.getGoodClickTimes();
            double c2=t1.getGoodClickTimes();
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

