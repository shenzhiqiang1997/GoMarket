package com.buhinia.search2.util;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.buhinia.search2.model.BrowseHistory;
import com.buhinia.search2.activity.GoodActivity;
import com.buhinia.search2.adapter.GoodAdapter;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.activity.MainActivity;
import com.buhinia.search2.activity.MarketActivity;
import com.buhinia.search2.adapter.MarketAdapter;
import com.buhinia.search2.model.MarketListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于发送请求的工具类
 */

public class HttpRequest {
    //发送请求的基地址
    public static final String rootPath="http://112.74.214.91:9025/?";

    //请求客户端,用于封装请求与发送请求
    private OkHttpClient okHttpClient=null;

    //Gson类库 用于解析返回的json数据
    private Gson gson=null;

    //两个标志变量 用于请求完毕后控制刷新控件
    boolean flag1=false;
    boolean flag2=false;


    //初始化
    public HttpRequest(){
        /**
         * 初始化请求客户端并配置
         * 超时时间为10秒
         */
        okHttpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .build();

        //初始化Gson
        gson=new Gson();

    }

    //用于请求单个商品信息
    public void requestForOneGood(String url,Activity activity){
        Request request=new Request.Builder().url(url).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"当前网络不可用,请检查你的网络设置",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                        GoodListItem goodListItem=null;
                        goodListItem= gson.fromJson(response.body().string(),GoodListItem.class);
                        GoodActivity.params.put(goodListItem.getGoodId(),goodListItem);
                }
            });

    }

    //用于请求单个超市信息
    public void requestForOneMarket(String url,Activity activity){
        Request request=new Request.Builder().url(url).build();


            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        MarketListItem marketListItem;
                        marketListItem= gson.fromJson(response.body().string(),MarketListItem.class);
                        MarketActivity.params.put(marketListItem.getMarketId(),marketListItem);
                    }else{
                        Toast.makeText(activity,"当前网络不可用,请检查你的网络设置",Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }

    //用于请求多个商品信息
    public void requestForLotsGood(String url, List<GoodListItem> goodList, GoodAdapter goodAdapter, Activity activity, SwipeRefreshLayout swipeRefreshLayout, Comparator<GoodListItem> comparator){
        flag1=false;
        Request request=new Request.Builder().url(url).build();
        try{
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!flag1&&!flag2){
                                if (swipeRefreshLayout!=null){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    flag1=true;
                    String responseData=response.body().string();
                    if (!goodList.isEmpty()){
                        goodList.clear();
                    }
                    List<GoodListItem> temGoodList=gson.fromJson(responseData,new TypeToken<List<GoodListItem>>(){}.getType());
                    for (GoodListItem goodListItem:temGoodList){
                        Response response1=okHttpClient.newCall(new Request.Builder().url(HttpRequest.rootPath+"Id=[m]".concat(String.valueOf(goodListItem.getMarketId()))).build()).execute();
                        MarketListItem marketListItem=gson.fromJson(response1.body().string(),MarketListItem.class);
                        goodListItem.setGoodDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                        goodListItem.setGoodSite(marketListItem.getMarketLocation());
                        goodList.add(goodListItem);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goodAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if (comparator!=null){
                        goodList.sort(comparator);

                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodAdapter.notifyDataSetChanged();
                        }
                    });
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout!=null&&flag1&&flag2){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    //用于请求全部商品信息
    public void requestForTagGood(String url, List<GoodListItem> goodList, GoodAdapter goodAdapter, Activity activity,SwipeRefreshLayout swipeRefreshLayout,Comparator<GoodListItem> comparator){
        LitePal.getDatabase();
        List<GoodListItem> temGoodList;
        if ((temGoodList=DataSupport.findAll(GoodListItem.class))!=null&&temGoodList.size()>0){
            try{
                if (!goodList.isEmpty()){
                    goodList.clear();
                }
                for (GoodListItem goodListItem:temGoodList){
                    Response response1=okHttpClient.newCall(new Request.Builder().url(HttpRequest.rootPath+"Id=[m]".concat(String.valueOf(goodListItem.getMarketId()))).build()).execute();
                    MarketListItem marketListItem=gson.fromJson(response1.body().string(),MarketListItem.class);
                    goodListItem.setGoodDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                    goodListItem.setGoodSite(marketListItem.getMarketLocation());
                    goodList.add(goodListItem);
                }
                if (comparator!=null){
                    goodList.sort(comparator);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goodAdapter.notifyDataSetChanged();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Request request=new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData=response.body().string();
                    if (!goodList.isEmpty()){
                        goodList.clear();
                    }
                    List<GoodListItem> temGoodList=gson.fromJson(responseData,new TypeToken<List<GoodListItem>>(){}.getType());
                    for (GoodListItem goodListItem:temGoodList){
                        Response response1=okHttpClient.newCall(new Request.Builder().url(HttpRequest.rootPath+"Id=[m]".concat(String.valueOf(goodListItem.getMarketId()))).build()).execute();
                        MarketListItem marketListItem=gson.fromJson(response1.body().string(),MarketListItem.class);
                        goodListItem.setGoodDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                        goodListItem.setGoodSite(marketListItem.getMarketLocation());
                        goodList.add(goodListItem);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goodAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if (comparator!=null){
                        goodList.sort(comparator);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodAdapter.notifyDataSetChanged();
                        }
                    });
                    LitePal.getDatabase();
                    DataSupport.deleteAll(GoodListItem.class);
                    DataSupport.saveAll(goodList);

                }
            });
        }

    }

    //用于请求多个超市信息
    public void requestForLotsMarket(String url, List<MarketListItem> marketList, MarketAdapter marketAdapter, Activity activity, SwipeRefreshLayout swipeRefreshLayout, Comparator<MarketListItem> comparator){
        flag2=false;
        Request request=new Request.Builder().url(url).build();
        try{
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!flag1&&!flag2){
                                if (swipeRefreshLayout!=null){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }

                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    flag2=true;
                    String responseData=response.body().string();
                    if (!marketList.isEmpty()) {
                        marketList.clear();
                    }
                    List<MarketListItem> temMarketList=gson.fromJson(responseData,new TypeToken<List<MarketListItem>>(){}.getType());
                    for (MarketListItem marketListItem:temMarketList){
                        marketListItem.setMarketDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                        marketList.add(marketListItem);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                marketAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if (comparator!=null){
                        marketList.sort(comparator);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            marketAdapter.notifyDataSetChanged();
                        }
                    });
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout!=null&&flag1||flag2){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    //用于请求全部超市信息
    public void requestForTagMarket(String url, List<MarketListItem> marketList, MarketAdapter marketAdapter,Activity activity, SwipeRefreshLayout swipeRefreshLayout,Comparator<MarketListItem> comparator){
        LitePal.getDatabase();
        List<MarketListItem> temMarketList;
        if ((temMarketList=DataSupport.findAll(MarketListItem.class))!=null&&temMarketList.size()>0){
            if (!marketList.isEmpty()) {
                marketList.clear();
            }
            for (MarketListItem marketListItem:temMarketList){
                marketListItem.setMarketDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                marketList.add(marketListItem);
            }
            if (comparator!=null){
                marketList.sort(comparator);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    marketAdapter.notifyDataSetChanged();
                }
            });
        }else{
            Request request=new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData=response.body().string();
                    if (!marketList.isEmpty()) {
                        marketList.clear();
                    }
                    List<MarketListItem>temMarketList=gson.fromJson(responseData,new TypeToken<List<MarketListItem>>(){}.getType());
                    for (MarketListItem marketListItem:temMarketList){
                        marketListItem.setMarketDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                        marketList.add(marketListItem);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                marketAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if (comparator!=null){
                        marketList.sort(comparator);
                    }
                    LitePal.getDatabase();
                    DataSupport.deleteAll(MarketListItem.class);
                    DataSupport.saveAll(marketList);

                }
            });
        }


    }

    //用于请求浏览历史
    public void requestForLotsGoodInHistory(List<BrowseHistory> historyList, List<GoodListItem> goodList, GoodAdapter goodAdapter, Activity activity){
        for (int i=historyList.size()-1;i>=0;i--){
            BrowseHistory history=historyList.get(i);
            LitePal.getDatabase();
            Request request=new Request.Builder().url(rootPath+"Id=[g]"+history.getGoodId()).build();
            Response response= null;
            GoodListItem goodListItem=null;
            try {
                response = okHttpClient.newCall(request).execute();
                goodListItem= gson.fromJson(response.body().string(),GoodListItem.class);
                Response response1=okHttpClient.newCall(new Request.Builder().url(rootPath+"Id=[m]"+goodListItem.getMarketId()).build()).execute();
                MarketListItem marketListItem=gson.fromJson(response1.body().string(),MarketListItem.class);
                goodListItem.setGoodDistance(Distance.getDistance(MainActivity.userLocation,marketListItem.getMarketLatitude(),marketListItem.getMarketLongitude()));
                goodListItem.setGoodSite(marketListItem.getMarketLocation());
                goodList.add(goodListItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goodAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Gson getGson() {
        return gson;
    }
}
