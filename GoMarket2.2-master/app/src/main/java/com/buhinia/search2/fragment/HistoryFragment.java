package com.buhinia.search2.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.buhinia.search2.adapter.GoodAdapter;
import com.buhinia.search2.R;
import com.buhinia.search2.model.BrowseHistory;
import com.buhinia.search2.model.GoodListItem;
import com.buhinia.search2.util.HttpRequest;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * 此fragment为浏览历史的fragment
 */

public class HistoryFragment extends Fragment {
    /*示例数据*/

    //历史商品集合
    private List<GoodListItem> goodList=new ArrayList<>();

    //浏览的商品id集合
    private List<BrowseHistory> historyList=new ArrayList<>();

    private GoodAdapter adapter;

    //用于发送请求的工具类
    private HttpRequest httpRequest=new HttpRequest();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载布局
        View view=inflater.inflate(R.layout.fragment_history,container,false);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);

        //获得浏览历史
        LitePal.getDatabase();
        historyList=DataSupport.findAll(BrowseHistory.class);

        //如果数据库中存在历史信息则载入,否则发送请求并存储浏览历史到数据库
        if (historyList!=null){
            if (!goodList.isEmpty()){
                goodList.clear();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpRequest.requestForLotsGoodInHistory(historyList,goodList,adapter,getActivity());
                }
            }).start();

        }


        return view;
    }



}
