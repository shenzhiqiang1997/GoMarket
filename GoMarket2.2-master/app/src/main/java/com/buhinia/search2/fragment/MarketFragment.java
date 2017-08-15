package com.buhinia.search2.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buhinia.search2.adapter.MarketAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.MarketListItem;
import com.buhinia.search2.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;


/*
* 此fragment为超市界面的fragment
* */

public class MarketFragment extends Fragment {

    //商家列表集合
    private List<MarketListItem> marketList=new ArrayList<>();

    private MarketAdapter marketAdapter1;

    //下拉刷新layout
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载布局
        View view=inflater.inflate(R.layout.fragment_market,container,false);

        RecyclerView recyclerView1=(RecyclerView)view.findViewById(R.id.market_fragment1_recycler_view);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        marketAdapter1=new MarketAdapter(marketList);
        recyclerView1.setAdapter(marketAdapter1);

        //下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.market_fragment_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh(){
                Refresh();
            }
        });

        //每次进入直接刷新列表,如果数据库中存在直接载入,否则发送请求并将信息保存在数据库
        Refresh();
        return view;
    }

    //刷新数据
    private void Refresh(){
        HttpRequest httpRequest=new HttpRequest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpRequest.requestForTagMarket(HttpRequest.rootPath+"Tag=[m]",marketList,marketAdapter1,getActivity(),swipeRefreshLayout,new MarketListItem.comparatorWithDistance());
            }
        }).start();
        swipeRefreshLayout.setRefreshing(false);
    }



}
