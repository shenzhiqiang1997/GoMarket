package com.buhinia.search2.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.buhinia.search2.adapter.GoodAdapter;
import com.buhinia.search2.adapter.MarketAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.ActivityListItem;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.HttpRequest;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/*
* 此fragment为首页的fragment
* */
public class FirstFragment extends Fragment {


    //这个为猜你喜欢的商品
    private List<GoodListItem> goodList=new ArrayList<>();
    private GoodAdapter goodAdapter;


    //这个为推荐商家
    private List<MarketListItem> marketList=new ArrayList<>();
    private MarketAdapter marketAdapter;

    //下拉刷新layout
    private SwipeRefreshLayout swipeRefreshLayout;

    //活动
    private List<ActivityListItem> activityList=new ArrayList<>();
    //活动布局
    private LinearLayout activity1;
    private LinearLayout activity2;
    private LinearLayout activity3;
    private LinearLayout activity4;
    private LinearLayout activity5;
    private LinearLayout activity6;
    private LinearLayout activity7;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //这个为猜你喜欢的商品
        View view=inflater.inflate(R.layout.fragment_first,container,false);
        RecyclerView recyclerView1=(RecyclerView)view.findViewById(R.id.first_market_recycler_view);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        marketAdapter=new MarketAdapter(marketList);
        recyclerView1.setAdapter(marketAdapter);

        //这个为推荐商家
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.first_good_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        goodAdapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(goodAdapter);

        //这个为活动

        activity1=(LinearLayout)view.findViewById(R.id.huodong1);
        activity2=(LinearLayout)view.findViewById(R.id.huodong2);
        activity3=(LinearLayout)view.findViewById(R.id.huodong3);
        activity4=(LinearLayout)view.findViewById(R.id.huodong4);
        activity5=(LinearLayout)view.findViewById(R.id.huodong5);
        activity6=(LinearLayout)view.findViewById(R.id.huodong6);
        activity7=(LinearLayout)view.findViewById(R.id.huodong7);

        //下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.first_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh(){
                Refresh();
            }
        });

        //获取数据库为获得数据做准备
        LitePal.getDatabase();

        //如果数据库存在了活动信息则直接载入,否则向服务器请求
        if(DataSupport.findAll(ActivityListItem.class)==null){
            RefreshForActivity();
        }else {
            setListenerForActivity(DataSupport.findAll(ActivityListItem.class));
        }
        //直接发送请求存在Bug 暂时不考虑
//        RefreshForHotGood();
//        RefreshForHotMarket();

        return view;
    }

    //刷新所有数据,当下拉刷新控件时调用
    private void Refresh(){
        HttpRequest httpRequest=new HttpRequest();
        RefreshForActivity();
        RefreshForHotMarket();
        RefreshForHotGood();

    }

    //仅仅刷新热推商品
    private void RefreshForHotGood(){
        HttpRequest httpRequest=new HttpRequest();
        httpRequest.requestForLotsGood(HttpRequest.rootPath+"Hot=[g]",goodList,goodAdapter,getActivity(),swipeRefreshLayout,new GoodListItem.comparatorWithDistance());
    }

    //仅仅刷新热推超市
    private void RefreshForHotMarket(){
        HttpRequest httpRequest=new HttpRequest();
        httpRequest.requestForLotsMarket(HttpRequest.rootPath+"Hot=[m]",marketList,marketAdapter,getActivity(),swipeRefreshLayout,new MarketListItem.comparatorWithDistance());
    }

    //为活动控件设置监听事件
    private void setListenerForActivity(List<ActivityListItem> activityList){
        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(0).getWebUrl()));
                startActivity(intent);
            }
        });
        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(1).getWebUrl()));
                startActivity(intent);
            }
        });
        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(2).getWebUrl()));
                startActivity(intent);
            }
        });
        activity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(3).getWebUrl()));
                startActivity(intent);
            }
        });
        activity5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(4).getWebUrl()));
                startActivity(intent);
            }
        });
        activity6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(5).getWebUrl()));
                startActivity(intent);
            }
        });
        activity7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(activityList.get(6).getWebUrl()));
                startActivity(intent);
            }
        });

    }

    //刷新活动信息,并保存到数据库
    private void RefreshForActivity(){
        HttpRequest httpRequest=new HttpRequest();
        Request request=new Request.Builder().url(HttpRequest.rootPath+"Jump=").build();
        httpRequest.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activityList.clear();
                activityList.addAll(httpRequest.getGson().fromJson(response.body().string(),new TypeToken<List<ActivityListItem>>(){}.getType()));
                if (!activityList.isEmpty()&&activityList!=null){
                    LitePal.getDatabase();
                    DataSupport.deleteAll(ActivityListItem.class);
                    DataSupport.saveAll(activityList);
                    setListenerForActivity(activityList);
                }


            }
        });
    }


}
